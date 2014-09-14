/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.network.mu.packets.MuWritePacket;

/**
 * @author Vlad
 *
 */
public class DeleteObject extends MuWritePacket {

	private int[] knwonList;

	public DeleteObject() { /* Default 1 object */ }

	public DeleteObject(int[] knwonList) {
		this.knwonList = knwonList;
	}

	@Override
	public void write(MuClient client, ByteBuf buff, Object... params) {
		if(knwonList != null) {
			writeArray(buff, 0xC1, 0x06, 0x14);
			writeC(buff, knwonList.length); // Count of ids to forget

			for (int i = 0; i < knwonList.length; i++) {
				int wi = knwonList[0];
				writeSh(buff, wi, ByteOrder.BIG_ENDIAN);
			}

			return;
		}
		
		GameObject entity = as(params[0]);
		
		writeArray(buff, 0xC1, 0x06, 0x14);
		writeC(buff, 0x01); // Count of ids to forget
		writeSh(buff, entity.getWorldIndex(), ByteOrder.BIG_ENDIAN);
	}

}
