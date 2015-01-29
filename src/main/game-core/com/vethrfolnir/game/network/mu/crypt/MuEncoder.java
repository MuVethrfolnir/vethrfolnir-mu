/**
 * Copyright (C) 2013-2014 Project-Vethrfolnir
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.game.network.mu.crypt;

import static com.vethrfolnir.game.network.mu.crypt.MuCryptUtils.GetHeaderSize;
import static com.vethrfolnir.game.network.mu.crypt.MuCryptUtils.GetPacketSize;
import io.netty.buffer.*;

import java.io.File;
import java.nio.ByteOrder;

import com.vethrfolnir.services.assets.AssetManager;

import corvus.corax.*;
import corvus.corax.config.CorvusConfig;

/**
 * @author Vlad
 *
 */
public final class MuEncoder {
	//private static final MuLogger log = MuLogger.getLogger(MuEncoder.class);
	
	// Internal use
	private static ByteBufAllocator alloc = //UnpooledByteBufAllocator.DEFAULT;
											PooledByteBufAllocator.DEFAULT;


	public static ByteBuf EncodePacket(ByteBuf buff, int serial) {
		int header = GetHeaderSize(buff);
		int packetSize = GetPacketSize(buff);
		int contentSize = packetSize - header;

		int encodedSize = (((contentSize / 8) + (((contentSize % 8) > 0) ? 1 : 0)) * 11) + header;
		
		int size = header;
		int originalHead = buff.getUnsignedByte(0);
		
		ByteBuf out = alloc.heapBuffer(encodedSize, encodedSize);

		//buff.writerIndex(buff.writerIndex() + 1);
		short[] Contents = new short[contentSize + 1];
        Contents[0] = (short) serial; // XXX:  Check this

        buff.readerIndex(1);
        
        //XXX When a c4.. 
        buff.setByte(1, serial);
        
        MuCryptUtils.readAsUByteArray(buff, Contents);
        
        //System.out.println("Encoding: "+PrintData.printData(Contents));
        
		size += EncodeBuffer(Contents, out, header, (contentSize + 1));

		out.writerIndex(0);
		
		// Header
		out.writeByte(originalHead);
		
		// Size write
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

        return out;
	}


	/**
	 * @param contents
	 * @param out
	 * @param header
	 * @param Size
	 * @param toServer 
	 * @return 
	 */
	private static int EncodeBuffer(short[] contents, ByteBuf out, int header, int Size) {
        int i = 0;
        int EncSize = 0;
        
        while (i < Size)
        {
        	short[] Encrypted = new short[11];
        	
            if (i + 8 < Size)
            {
            	short[] Decrypted = new short[8];
                System.arraycopy(contents, i, Decrypted, 0, 8);
                BlockEncode(Encrypted, Decrypted, 8, MuKeyFactory.getServerToClientPacketEncKeys());
            }
            else
            {
            	short[] Decrypted = new short[Size - i];
            	System.arraycopy(contents, i, Decrypted, 0, Decrypted.length);
            	
                BlockEncode(Encrypted, Decrypted, (Size - i), MuKeyFactory.getServerToClientPacketEncKeys());
            }
            
            out.writerIndex(header + EncSize);
            for (int j = 0; j < Encrypted.length; j++) {
                out.writeByte(Encrypted[j]);
			}
            
            i += 8;
            EncSize += 11;
        }
        
        return EncSize;
	}


	/**
	 * @param encrypted
	 * @param decrypted
	 * @param convertor 
	 * @param Size
	 * @param Keys
	 */
	private static void BlockEncode(short[] OutBuf, short[] InBuf, int Size, long[] Keys) {
        short[] Finale = new short[2];
        Finale[0] = (short) Size;
        Finale[0] ^= 0x3D;
        Finale[1] = 0xF8;
        
        for (int i = 0; i < Size; i++)
            Finale[1] ^= InBuf[i];
        
        Finale[0] ^= Finale[1];
        
        ShiftBytes(OutBuf, 0x48, Finale, 0x00, 0x10);
        
        //System.err.println("Encode block: ------------------ Size: "+Size);
        //System.err.println(PrintData.printData(InBuf));
        //System.err.println();
        
        long[] Ring = new long[4];
        char[] CryptBuf = new char[4];

        // Here was a block copy, that wasent really needed
       	CryptBuf[0] = convertShort(InBuf[0], InBuf.length > 1 ? InBuf[1] : 0x00);
       	CryptBuf[1] = convertShort(InBuf.length > 2 ? InBuf[2] : 0x00, InBuf.length > 3 ? InBuf[3] : 0x00);
       	CryptBuf[2] = convertShort(InBuf.length > 4 ? InBuf[4] : 0x00, InBuf.length > 5 ? InBuf[5] : 0x00);
       	CryptBuf[3] = convertShort(InBuf.length > 6 ? InBuf[6] : 0x00, InBuf.length > 7 ? InBuf[7] : 0x00);

       	Ring[0] = ((Keys[8] ^ (CryptBuf[0])) * Keys[4]) % Keys[0];
        Ring[1] = ((Keys[9] ^ (CryptBuf[1] ^ (Ring[0] & 0xFFFF))) * Keys[5]) % Keys[1];
        Ring[2] = ((Keys[10] ^ (CryptBuf[2] ^ (Ring[1] & 0xFFFF))) * Keys[6]) % Keys[2];
        Ring[3] = ((Keys[11] ^ (CryptBuf[3] ^ (Ring[2] & 0xFFFF))) * Keys[7]) % Keys[3];

        //Buffer.BlockCopy(CryptBuf, 0, InBuf, 0, (int)Size);
        
        Ring[0] = Ring[0] ^ Keys[8] ^ (Ring[1] & 0xFFFF);
        Ring[1] = Ring[1] ^ Keys[9] ^ (Ring[2] & 0xFFFF);
        Ring[2] = Ring[2] ^ Keys[10] ^ (Ring[3] & 0xFFFF);

        //Current ring here 1d4b 57ae 74ce 8866 
        short[] Shift = new short[4];
        
        // 4B 1D 00 00 - after block copy must achive this
        //Buffer.BlockCopy(Ring, 0, Shift, 0, 4);
        copyRingArray(Ring[0], Shift);
        ShiftBytes(OutBuf, 0x00, Shift, 0x00, 0x10);
        ShiftBytes(OutBuf, 0x10, Shift, 0x16, 0x02);

        //Buffer.BlockCopy(Ring, 4, Shift, 0, 4);
        copyRingArray(Ring[1], Shift);
        ShiftBytes(OutBuf, 0x12, Shift, 0x00, 0x10);
        ShiftBytes(OutBuf, 0x22, Shift, 0x16, 0x02);
        
        //Buffer.BlockCopy(Ring, 8, Shift, 0, 4);
        copyRingArray(Ring[2], Shift);
        ShiftBytes(OutBuf, 0x24, Shift, 0x00, 0x10);
        ShiftBytes(OutBuf, 0x34, Shift, 0x16, 0x02);
        
        //Buffer.BlockCopy(Ring, 12, Shift, 0, 4);
        copyRingArray(Ring[3], Shift);
        ShiftBytes(OutBuf, 0x36, Shift, 0x00, 0x10);
        ShiftBytes(OutBuf, 0x46, Shift, 0x16, 0x02);
	}

	private static void copyRingArray(long ring, short[] shift)
    {
    	short[] ss = intToByteArray((int) ring);
    	for (int i = 0; i < ss.length; i++) {
			shift[i] = (short) (ss[i]);
		}
    }
    
	private static final short[] intToByteArray(int value) { // little endiness
		return new short[] { (short) (value & 0xff), (short) (value >> 8 & 0xff), (short) (value >> 16 & 0xff), (short) (value >>> 24) };
	}
    
	private static char convertShort(int b1, int b2) {
        char result = (char) (b1 & 0xff);
        result |= b2 << 8 & 0xff00;
		return result;
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
            for (int i = 0; i < Size;++i)
                OutBuf[(int) (i + (Arg_4 / 8))] = (short) ((byte)(OutBuf[(int) (i + (Arg_4 / 8))] | Tmp[i]) & 0xFF);
        
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

    
    public static void main(String[] args) {
    	Corax.Install(new CoraxBuilder() {
			
			@Override
			protected void build(Corax corax) {
				CorvusConfig.WorkingDirectory = new File("./dist/GameServer");
				Corax.config().loadDirectory("/config");
				bind(AssetManager.class).as(Scope.Singleton);;
			}
    	});
    	
    	MuKeyFactory.parse();
        System.out.println("");

		//byte[] data = PacketUtils.hex2Bytes("C3 44 F3 03 42 E6 33 00 00 00 00 00 00 00 FA 00 00 00 00 00 00 01 25 84 0A 00 2D 00 32 00 12 00 28 00 56 00 56 00 59 00 59 00 C8 00 C8 00 10 00 20 00 12 00 B0 B4 1D 00 03 00 00 00 04 00 00 00 00 00 04 00");
    	byte[] data = PacketUtils.hex2Bytes("C4 00 2D 01 F3 10 03 00 00 00 12 00 00 10 00 FF FF FF FF FF 0C 14 08 1E 00 00 D0 00 FF FF FF FF FF 0D 14 10 1E 00 00 D0 00 FF FF FF FF");
		ByteBuf buff = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
		buff.writeBytes(data);
		
		ByteBuf out = EncodePacket(buff, 0x02);
		
		byte[] arr = new byte[out.readableBytes()];
		out.readBytes(arr);
		
		//System.out.println(PrintData.printData(out.nioBuffer()));
		//System.out.println("C3-65-22-EF-31-62-4B-D5-32-2F-B1-AA-9F-45-1B-08-45-D2-96-92-B1-51-37-02-45-1B-1F-83-13-D1-F3-9C-E6-67-52-9C-F7-0F-7C-CD-9B-E1-EC-B4-E8-DD-3D-33-0D-44-C2-73-52-48-4E-CD-F8-98-77-44-8B-99-FA-64-71-A6-FD-C8-35-92-35-B9-95-3D-A2-3C-81-C5-F0-0F-4E-1D-F0-81-34-72-2B-E8-C9-FC-36-05-31-36-80-D0-80-BB-0C-C2-FC");
		System.out.println("C3-0D-FE-53-65-66-18-AB-51-01-C1-4D-77");
		System.out.println(PacketUtils.byteArrayToHex(arr).toUpperCase());
	}
}
