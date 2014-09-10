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
package com.vethrfolnir.game.network.mu.send;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class SkillListInfo extends WritePacket {

	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
        writeArray(buff, 0xc1, 0x1e, 0xf3, 0x11, 0x02, 0x00); 
        writeArray(buff, 0x00, 0x00, 0x11, 0x00);
        writeArray(buff, 01, 0x00, 0x2d, 0x00); 
	}

}
