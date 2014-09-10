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
package com.vethrfolnir.login.network.game.send;

import io.netty.buffer.ByteBuf;

import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.WritePacket;

/**
 * @author Vlad
 *
 */
public class KillPacket extends WritePacket {

	public enum KillType
	{
		INCORRECT_PASSWORD("The password is incorect."),
		INCORRECT_ID("The id is incorect."),
		ID_TAKEN("The id is already taken."), 
		NO_AVIALIABLE_ID("No availble id could be generated.");
		
		private final String _err;
		KillType(String str)
		{
			_err = str;
		}
		
		public String getErr() {
			return _err;
		}
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.network.WritePacket#write(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.lang.Object[])
	 */
	@Override
	public void write(NetworkClient context, ByteBuf buff, Object... params) {
		writeC(buff, 0xBB);
		writeS(buff, as(params[0], KillType.class).getErr());
	}
}
