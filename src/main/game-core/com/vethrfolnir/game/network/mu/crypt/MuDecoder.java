/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.game.network.mu.crypt;

import static com.vethrfolnir.game.network.mu.crypt.MuCryptUtils.*;
import io.netty.buffer.*;

import java.nio.ByteOrder;

import com.vethrfolnir.logging.MuLogger;

import corvus.corax.tools.PrintData;

/**
 * @author Vlad
 *
 */
public final class MuDecoder {
    
	private static final MuLogger log = MuLogger.getLogger(MuDecoder.class);
	
	// Internal use
	private static ByteBufAllocator alloc = //UnpooledByteBufAllocator.DEFAULT;
											PooledByteBufAllocator.DEFAULT;
	
    // GMO KEYS
    private static short[] Xor32Keys = new short[] {
    	0xAB, 0x11, 0xCD, 0xFE, 0x18, 0x23, 0xC5, 0xA3, 
    	0xCA, 0x33, 0xC1, 0xCC, 0x66, 0x67, 0x21, 0xF3, 
    	0x32, 0x12, 0x15, 0x35, 0x29, 0xFF, 0xFE, 0x1D, 
    	0x44, 0xEF, 0xCD, 0x41, 0x26, 0x3C, 0x4E, 0x4D
    };

	public static ByteBuf DecodeXor32(ByteBuf buff) {
		
		if(buff.readerIndex() != 0) {
			log.warn("Buffer must be at index 0!");
			buff.readerIndex(0);
		}
		
		int header = GetHeaderSize(buff);
		int decodedSize = GetDecodedSize(buff);

		buff.readerIndex(header);
		DecXor32(buff, header, decodedSize - header);
		buff.readerIndex(0);
		return buff;
	}
	
	/**
	 * C3 C4
	 * @param buff
	 * @return 
	 */
	public static ByteBuf DecodePacket(ByteBuf buff) {
		if(buff.writerIndex() <= 2) {
			log.fatal("Ambiguous buffer! "+ByteBufUtil.hexDump(buff));
			return null;
		}
		if(buff.readerIndex() != 0) {
			log.warn("Buffer must be at index 0!");
			buff.readerIndex(0);
		}
		
		int header = GetHeaderSize(buff);
		int packetSize = GetPacketSize(buff);
		int decodedSize = GetDecodedSize(buff);
		
		int contentSize = packetSize - header;

		//System.out.println("Header[0x"+PrintData.fillHex(buff.getUnsignedByte(0), 2)+"] size: "+GetHeaderSize(buff)+" Packet Size: "+GetPacketSize(buff) +" Content Size: "+contentSize +" Decoded: "+ GetDecodedSize(buff));

		ByteBuf out = alloc.heapBuffer(decodedSize, decodedSize);
		
		int originalHead = buff.getUnsignedByte(0);
		
		buff.readerIndex(header);
		int size = DecodeBlock(buff, out, header, contentSize);
		//buff.clear();
		
        size += header-1;
        
        out.writerIndex(0);
        out.writeByte(originalHead);
        
        switch (originalHead)
        {
        	case 0xc3:
        		out.writeByte(size);
        		break;
            case 0xC4:
            	out.writeByte(size >> 8);
            	out.writeByte(size & 0xFF);
                break;
        }
        
        out.writerIndex(size);
		out.readerIndex(header);
		
		DecXor32(out, header, size);

		out.readerIndex(0);
		return out;
	}
	
	private static int DecodeBlock(ByteBuf buff, ByteBuf outBuff, int offset, int size) {
		// decripted size
		int index = 0;

        if ((size % 11) != 0)
        {
        	log.warn("Cannot decrypt packet, it's already decrypted!: Size "+size+" = "+((size % 11)));
        	log.warn(PrintData.printData(buff.nioBuffer()));
            return -1;
        }

    	ByteBuf encrypted = alloc.heapBuffer(11, 11).order(ByteOrder.LITTLE_ENDIAN);
    	short[] uByteArray = new short[encrypted.capacity()];
    	
    	ByteBuf decrypted = alloc.heapBuffer(8, 8).order(ByteOrder.LITTLE_ENDIAN);
		ByteBuf converter = alloc.heapBuffer(4).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < size; i += 11) {
        	buff.readBytes(encrypted);
        	
        	//System.out.println("ENC: "+PrintData.printData(encrypted.nioBuffer()));
            int Result = BlockDecode(decrypted, getAsUByteArray(encrypted, uByteArray), converter, MuKeyFactory.getClientToServerPacketDecKeys());
            if (Result != -1)
            {
                //Buffer.BlockCopy(Decrypted, 0, m_DecryptResult, (OffSet - 1) + DecSize, Result);
            	
            	outBuff.writerIndex((offset - 1) + index);
            	outBuff.writeBytes(decrypted);
        		

            	//outBuff.writeBytes(decrypted);

        		decrypted.clear();
        		encrypted.clear();
        		converter.clear();
            	//System.arraycopy(Decrypted, 0, m_DecryptResult, (OffSet - 1) + DecSize, Result);
            	
                index += Result;
            }
        }

		return index;
	}

	/**
	 * @param decrypted
	 * @param encrypted
	 * @param decServerKeys
	 * @return
	 */
	private static int BlockDecode(ByteBuf decrypted, short[] InBuf, ByteBuf converter, long[] Keys) {

		long[] Ring = new long[4];
		short[] Shift = new short[4];

		ShiftBytes(Shift, 0x00, InBuf, 0x00, 0x10);
		ShiftBytes(Shift, 0x16, InBuf, 0x10, 0x02);

		writeByteArray(converter, Shift);
		flushArray(Shift, 0, 4);

		ShiftBytes(Shift, 0x00, InBuf, 0x12, 0x10);
		ShiftBytes(Shift, 0x16, InBuf, 0x22, 0x02);

		writeByteArray(converter, Shift);
		flushArray(Shift, 0, 4);

		ShiftBytes(Shift, 0x00, InBuf, 0x24, 0x10);
		ShiftBytes(Shift, 0x16, InBuf, 0x34, 0x02);

		writeByteArray(converter, Shift);
		flushArray(Shift, 0, 4);

		ShiftBytes(Shift, 0x00, InBuf, 0x36, 0x10);
		ShiftBytes(Shift, 0x16, InBuf, 0x46, 0x02);

		writeByteArray(converter, Shift);
		flushArray(Shift, 0, 4);

		// for (int i = 0; i < Ring.length; i++) {
		// System.err.print(Integer.toHexString((int) Ring[i])+" ");;
		// }

		// System.err.println();

		for (int i = 0; i < Ring.length; i++) {
			Ring[i] = converter.readInt();
		}
		converter.clear();
		
		Ring[2] = Ring[2] ^ Keys[10] ^ (Ring[3] & 0xFFFF);
		Ring[1] = Ring[1] ^ Keys[9] ^ (Ring[2] & 0xFFFF);
		Ring[0] = Ring[0] ^ Keys[8] ^ (Ring[1] & 0xFFFF);
		

//		 System.err.println("Finished Ring: ");
//		 for (int i = 0; i < Ring.length; i++) {
//		 System.err.print(Integer.toHexString((int) Ring[i])+" ");;
//		 }
//		 System.err.println();
		int[] CryptBuf = new int[4];

		// Had ushort cast here.
		CryptBuf[0] = (int) (Keys[8] ^ ((Ring[0] * Keys[4]) % Keys[0]));

		CryptBuf[1] = (int) (Keys[9] ^ ((Ring[1] * Keys[5]) % Keys[1]) ^ (Ring[0] & 0xFFFF));
		CryptBuf[2] = (int) (Keys[10] ^ ((Ring[2] * Keys[6]) % Keys[2]) ^ (Ring[1] & 0xFFFF));
		CryptBuf[3] = (int) (Keys[11] ^ ((Ring[3] * Keys[7]) % Keys[3]) ^ (Ring[2] & 0xFFFF));

//		System.err.println("Pre done: " + PrintData.printData(CryptBuf));

		short[] Finale = new short[2];
		ShiftBytes(Finale, 0x00, InBuf, 0x48, 0x10);
		Finale[0] ^= Finale[1];
		Finale[0] ^= 0x3D;

		converter.clear();
		for (int i = 0; i < CryptBuf.length; i++) {
			converter.writeShort(CryptBuf[i]);
		}
		
		decrypted.writeBytes(converter, Finale[0]);
		converter.clear();

		//System.out.println(PrintData.printData(decrypted.nioBuffer()));

		// System.err.println("Finale: "+ Finale[0]);

		short Check = 0xF8;
		for (int i = 0; i < Finale[0]; ++i)
			Check = (short) (Check ^ decrypted.getUnsignedByte(i));

		if (Finale[1] == Check)
			return Finale[0];

		//System.err.println("Finale["+Finale[0]+"] And done: "+PrintData.printData(decrypted.nioBuffer()));

		return Finale[0];
	}

	private static long ShiftBytes(short[] OutBuf, long Arg_4, short[] InBuf, long Arg_C, long Arg_10)
    {
        long Size = ((((Arg_10 + Arg_C) - 1) / 8) + (1 - (Arg_C / 8)));
        
        short[] Tmp = new short[20];
        System.arraycopy(InBuf,(int)(Arg_C / 8), Tmp, 0,(int)Size);
        
        long Var_4 = (Arg_10 + Arg_C) & 0x7;
        
        if (Var_4 !=0) // Pay attention, too many casts, look for negatives
        	Tmp[(int) (Size - 1)] = (short) (Tmp[(int) (Size-1)] & 0xFF << (int)(8 - Var_4));
        
        Arg_C &= 0x7;
       
        ShiftRight(Tmp, (int)Size, (int)Arg_C);
        ShiftLeft(Tmp, (int)Size + 1,(int)(Arg_4 & 0x7));

        if ((Arg_4 & 0x7) > Arg_C) ++Size;
        if (Size != 0)
            for (int i = 0; i < Size;++i) {
                OutBuf[(int) (i + (Arg_4 / 8))] = (short) ((byte)(OutBuf[(int) (i + (Arg_4 / 8))] | Tmp[i]) & 0xFF);
            }
        
        return Arg_10 + Arg_4;
    }

    private static void ShiftLeft(short[] Data, int Size, int Shift)
    {
        if(Shift == 0)return;
        for (int i = 1; i < Size; i++)
            Data[Size - i] = (byte) ((Data[Size - i] >> Shift) | ((Data[Size - i - 1]) << (8 - Shift)));
        
        Data[0] = (short) ((byte)(Data[0] >> Shift) & 0xFF);
    } 

    private static void ShiftRight(short[] Data, int Size, int Shift)
    {
    	
        if (Shift == 0) return;
        for (int i = 1; i < Size; i++)
            Data[i - 1] = (byte)((Data[i - 1] << Shift) | (Data[i] >> (8 - Shift)));
        
        Data[Size - 1] = (short) ((byte)(Data[Size - 1] << Shift) & 0xFF);
    }

    private static void DecXor32(ByteBuf buff, int SizeOfHeader, int Len) {

		for (int i = Len - 1; i > 0; i--) {
    		int Buff = buff.getUnsignedByte(buff.readerIndex() + i);
    		Buff ^= (Xor32Keys[(i+SizeOfHeader) & 31] ^ buff.getUnsignedByte((buff.readerIndex() + i)-1));
    		buff.setByte(buff.readerIndex() + i, Buff);
    	}
    }

	public static void main(String[] args) {
		//byte[] data = PacketUtils.hex2Bytes("C1 04 F3 0D d3");
		//byte[] data = PacketUtils.hex2Bytes("C1 0F F3 7B 85 58 A8 B9 E9 41 BF 09 40 1D 74");
		byte[] data = PacketUtils.hex2Bytes("C3 5A 9E 4D 18 56 28 FB 20 E5 2D A7 92 5A 01 33 CB 50 BA F0 10 69 76 43 16 D1 65 36 64 13 F1 45 CC 1A 2F B1 B7 4E 24 49 1F F2 2F 54 A7 92 F8 04 7E 8A A7 A7 73 EF 00 C9 FC E2 CF 35 E4 58 21 A0 5C 89 16 23 B2 8C 5C 4C 62 23 B2 8B 90 27 12 61 07 5D 00 4D C3 F3 5A 31 99 A7");

		
		ByteBuf buff = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
		
		ByteBuf out = null;
		for (int i = 0; i < 1; i++) {
			buff.writeBytes(data);
			
			long t1 = System.currentTimeMillis();
			//DecodeXor32(buff);
			out = DecodePacket(buff);
			long t2 = System.currentTimeMillis();
	
			System.out.println("Process time: "+(t2 - t1)+" milis");
		
		}
		System.out.println(PrintData.printData(out.nioBuffer()));
		
	}
}
