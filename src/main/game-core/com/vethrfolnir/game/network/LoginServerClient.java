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
package com.vethrfolnir.game.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.vethrfolnir.game.network.login.LoginServerClientHandler;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.*;
import com.vethrfolnir.services.threads.CorvusThreadPool;

import corvus.corax.Corax;
import corvus.corax.config.Config;
import corvus.corax.inject.Inject;

/**
 * @author Vlad
 *
 */
public final class LoginServerClient extends NetworkClient implements NetworkServerInterface, Runnable {

	private static final MuLogger log = MuLogger.getLogger(LoginServerClient.class);

	@Config(key = "LoginServer.Host", value = "0.0.0.0")
	private String host;

	@Config(key = "LoginServer.Port", value = "8081")
	private int port;
	
	private Bootstrap bootstrap;
	private ChannelFuture channelFuture;

	private NetworkClient client;

	public LoginServerClient() {
		super(null);
	}
	
	@Inject
	private void load() {
		log.info("Preparing To connect to login! Info[Host: "+host+", Port: "+port+"]");
		try {
			NioEventLoopGroup workerGroup = new NioEventLoopGroup();
			
            bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(
                             //new LoggingHandler(LogLevel.INFO)
                    		 new LoginServerClientHandler(LoginServerClient.this)
                    		 );
                 }
             });
		}
		catch(Exception e) {
			log.fatal("Unable to set up netty!", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.login.network.NetworkServerInterface#start()
	 */
	@Override
	public void start() {
		// Reload
		if(bootstrap == null) {
			load();
		}

		Corax.fetch(CorvusThreadPool.class).executeLongRunning(this);
	}

	@Override
	public void stop() {
		if(channelFuture != null)
			channelFuture.channel().close();
	}
	
	@Override
	public void run() {
		try {
	        // Start the server.
	        channelFuture = bootstrap.connect(host, port);
	        
	        log.info("Connected on login "+channelFuture.channel());
	        
	        channelFuture.sync();
	
	        // Wait until the server socket is closed.
	        channelFuture.channel().closeFuture().sync();
		}
		catch(Exception e) {
			log.fatal("Network thread fail!", e);
		}
        finally {
            // Shut down all event loops to terminate all threads.
        	bootstrap.group().shutdownGracefully();
            bootstrap = null;
            channelFuture = null;
            
            try {
                log.info("Restarting!");
				Thread.sleep(2000);
	            start();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}

	@Override
	public Channel channel() {
		return channelFuture.channel();
	}
	
	@Override
	public void sendPacket(WritePacket packet, Object... params) {
		ByteBuf buff = client.alloc().buffer();
		packet.write(client, buff, params);
		client.writeAndFlush(buff);
	}

	/**
	 * @param ctx
	 */
	public void create(final ChannelHandlerContext ctx) {
		client = new NetworkClient(channel()) {

		@Override
		public void sendPacket(WritePacket packet, Object... params) {

		}};
	}
	
}
