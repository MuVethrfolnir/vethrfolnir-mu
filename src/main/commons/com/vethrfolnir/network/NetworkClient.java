/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.network;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

/**
 * @author Vlad
 *
 */
public abstract class NetworkClient {

	private final Channel channel;

	public NetworkClient(Channel channel) {
		this.channel = channel;
	}

	/**
	 * @return the channel
	 */
	public Channel channel() {
		return channel;
	}
	
	public ByteBufAllocator alloc() {
		return channel().alloc();
	}

	public abstract void sendPacket(WritePacket packet, Object... params);
	
	public void writeAndFlush(Object obj) {
		channel().writeAndFlush(obj);
	}
	
	public void close() {
		channel().close();
	}
}
