/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.vethrfolnir.game.network.login.LoginServerClientHandler;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.NetworkClient;
import com.vethrfolnir.network.NetworkServerInterface;
import com.vethrfolnir.network.WritePacket;

import corvus.corax.Corax;
import corvus.corax.processing.annotation.Config;
import corvus.corax.processing.annotation.Initiate;
import corvus.corax.threads.CorvusThreadPool;

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
	
	@Initiate
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

		Corax.getInstance(CorvusThreadPool.class).executeLongRunning(this);
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.login.network.NetworkServerInterface#stop()
	 */
	@Override
	public void stop() {
		if(channelFuture != null)
			channelFuture.channel().close();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
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
