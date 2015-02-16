/*
 This application is part of corvus engine, and its strictly bounded to its owner.
 Using corvus engine unauthorized is strictly forbidden.
*/
package com.vethrfolnir.game.network.mu.send;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;


/**
 * @author Seth
 */
public final class CreatureSay extends MuWritePacket{

	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		int objId = (int) params[0];
		String message = (String) params[1];
		
		writeC(buff, 0xC1);
		writeC(buff, 0x15); // size
		writeC(buff, 0x01); // opcode
		writeSh(buff, objId, ByteOrder.BIG_ENDIAN);
		writeS(buff, message, message.length() + 5); //message
	}
}
