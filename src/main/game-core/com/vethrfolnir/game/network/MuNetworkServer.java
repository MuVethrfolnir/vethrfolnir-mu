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
package com.vethrfolnir.game.network;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.vethrfolnir.game.network.mu.MuChannelHandler;
import com.vethrfolnir.game.network.mu.MuCyperDecoder;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.network.NetworkServerInterface;

import corvus.corax.Corax;
import corvus.corax.processing.annotation.Config;
import corvus.corax.processing.annotation.Initiate;
import corvus.corax.threads.CorvusThreadPool;

/**
 * @author Vlad
 *
 */
public final class MuNetworkServer implements NetworkServerInterface, Runnable {

	private static final MuLogger log = MuLogger.getLogger(MuNetworkServer.class);
	
	@Config(key = "Game.Host", value = "0.0.0.0")
	private String host;
	
	@Config(key = "Fame.Port", value = "55901")
	private int port;

	private ServerBootstrap bootstrap;
	private ChannelFuture channelFuture;
	
	public static final AtomicInteger onlineClients = new AtomicInteger();
	
	@Initiate
	private void load() {
		
		log.info("Preparing Game server listening! Info[Host: "+host+", Port: "+port+"]");
		try {
			NioEventLoopGroup bossGroup = new NioEventLoopGroup();
			NioEventLoopGroup workerGroup = new NioEventLoopGroup();
			
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 100)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(
                    		 new MuCyperDecoder(),
                    		 new MuChannelHandler()
                             //new MuCyperEncoder()
                    		 );
                 }
             });
		}
		catch(Exception e) {
			log.fatal("Unable to set up netty!", e);
		}
	}
	
	@Override
	public void start() {
		// Reload
		if(bootstrap == null) {
			load();
		}

		Corax.getInstance(CorvusThreadPool.class).executeLongRunning(this);
	}

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
	        channelFuture = bootstrap.bind(host, port);
	        
	        log.info("Listening on[Host: "+host+", Port: "+port+"]");
	        
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
        	bootstrap.childGroup().shutdownGracefully();
            bootstrap = null;
            channelFuture = null;
        }
	}

	/**
	 * @return
	 */
	public static int onlineClients() {
		return onlineClients.get();
	}
}
