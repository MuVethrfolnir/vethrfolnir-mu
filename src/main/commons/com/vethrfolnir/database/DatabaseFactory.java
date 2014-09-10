/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.vethrfolnir.logging.MuLogger;

import corvus.corax.CorvusConfig;
import corvus.corax.processing.annotation.Initiate;

/**
 * @author Vlad
 *
 */
public class DatabaseFactory {

	private static MuLogger log = MuLogger.getLogger(DatabaseFactory.class);
	
	private BoneCP _connectionPool = null;

	
	@Initiate
	private void load() {
		
		try
		{
			// load the database driver
			Class.forName(CorvusConfig.getProperty("Database.Driver", "com.mysql.jdbc.Driver"));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Failed initializing jdbc driver!", e);
			throw new RuntimeException("System critical fail!");
		}

		try {
			BoneCPConfig config = new BoneCPConfig();
			
			/**
			 * Sets the JDBC connection URL.
			 */
			config.setJdbcUrl(CorvusConfig.getProperty("Database.Url", "jdbc:mysql://localhost/") +
					CorvusConfig.getProperty("Database.Name", "vethrfolniremu"));
			
			/**
			 * Sets username to use for connections.
			 */
			config.setUsername(CorvusConfig.getProperty("Database.User", "root"));
			
			/**
			 * Sets password to use for connections.
			 */
			config.setPassword(CorvusConfig.getProperty("Database.Password", ""));
			
			/**
			 * Sets the acquireIncrement property. When the available connections are about to run
			 * out, BoneCP will dynamically create new ones in batches. This property controls how
			 * many new connections to create in one go (up to a maximum of
			 * maxConnectionsPerPartition). Note: This is a per partition setting.
			 */
			config.setAcquireIncrement(2);
			
			/**
			 * Sets number of partitions to use. In order to reduce lock contention and thus improve
			 * performance, each incoming connection request picks off a connection from a pool that
			 * has thread-affinity, i.e. pool[threadId % partition_count]. The higher this number,
			 * the better your performance will be for the case when you have plenty of short-lived
			 * threads. Beyond a certain threshold, maintenance of these pools will start to have a
			 * negative effect on performance (and only for the case when connections on a partition
			 * start running out). Default: 2, minimum: 1, recommended: 2-4
			 */
			config.setPartitionCount(CorvusConfig.getProperty("Database.PartitionCount", 2));
			
			/**
			 * Sets the minimum number of connections that will be contained in every partition.
			 */
			config.setMinConnectionsPerPartition(CorvusConfig.getProperty("Database.MinConnections", 2));
			
			/**
			 * Sets the maximum number of connections that will be contained in every partition.
			 * Setting this to 5 with 3 partitions means you will have 15 unique connections to the
			 * database. Note that the connection pool will not create all these connections in one
			 * go but rather start off with minConnectionsPerPartition and gradually increase
			 * connections as required.
			 */
			config.setMaxConnectionsPerPartition(CorvusConfig.getProperty("Database.MaxConnections", 10));
			
			/**
			 * Sets the idleConnectionTestPeriod. This sets the time (in minutes), for a connection
			 * to remain idle before sending a test query to the DB. This is useful to prevent a DB
			 * from timing out connections on its end. Do not use aggressive values here! Default:
			 * 240 min, set to 0 to disable
			 */
			config.setIdleConnectionTestPeriodInMinutes(240);
			
			/**
			 * Sets the idleConnectionTestPeriod. This sets the time (in seconds), for a connection
			 * to remain idle before sending a test query to the DB. This is useful to prevent a DB
			 * from timing out connections on its end. Do not use aggressive values here! Default:
			 * 240 min, set to 0 to disable
			 */
			config.setIdleConnectionTestPeriodInSeconds(14400);
			
			/**
			 * Sets Idle max age (in min). The time (in minutes), for a connection to remain unused
			 * before it is closed off. Do not use aggressive values here! Default: 60 minutes, set
			 * to 0 to disable.
			 */
			//config.setIdleMaxAgeInMinutes(60);
			
			/**
			 * Sets Idle max age (in seconds). The time (in seconds), for a connection to remain
			 * unused before it is closed off. Do not use aggressive values here! Default: 60
			 * minutes, set to 0 to disable.
			 */
			//config.setIdleMaxAgeInSeconds(3600);
			
			/**
			 * Sets statementsCacheSize setting. The number of statements to cache.
			 */
			config.setStatementsCacheSize(0);
			
			/**
			 * Sets number of helper threads to create that will handle releasing a connection. When
			 * this value is set to zero, the application thread is blocked until the pool is able
			 * to perform all the necessary cleanup to recycle the connection and make it available
			 * for another thread. When a non-zero value is set, the pool will create threads that
			 * will take care of recycling a connection when it is closed (the application dumps the
			 * connection into a temporary queue to be processed asychronously to the application
			 * via the release helper threads). Useful when your application is doing lots of work
			 * on each connection (i.e. perform an SQL query, do lots of non-DB stuff and perform
			 * another query), otherwise will probably slow things down.
			 */
			//config.setReleaseHelperThreads(3);
			
			/**
			 * Instruct the pool to create a helper thread to watch over connection acquires that
			 * are never released (or released twice). This is for debugging purposes only and will
			 * create a new thread for each call to getConnection(). Enabling this option will have
			 * a big negative impact on pool performance.
			 */
			config.setCloseConnectionWatch(false);
			
			/**
			 * If enabled, log SQL statements being executed.
			 */
			config.setLogStatementsEnabled(false);
			
			/**
			 * Sets the number of ms to wait before attempting to obtain a connection again after a
			 * failure.
			 */
			config.setAcquireRetryDelayInMs(7000);
			
			/**
			 * Set to true to force the connection pool to obtain the initial connections lazily.
			 */
			config.setLazyInit(false);
			
			/**
			 * Set to true to enable recording of all transaction activity and replay the
			 * transaction automatically in case of a connection failure.
			 */
			config.setTransactionRecoveryEnabled(false);
			
			/**
			 * After attempting to acquire a connection and failing, try to connect these many times
			 * before giving up. Default 5.
			 */
			config.setAcquireRetryAttempts(5);
			
			/**
			 * Queries taking longer than this limit to execute are logged.
			 */
			config.setQueryExecuteTimeLimitInMs(0);
			
			/**
			 * Sets the Pool Watch thread threshold. The pool watch thread attempts to maintain a
			 * number of connections always available (between minConnections and maxConnections).
			 * This value sets the percentage value to maintain. For example, setting it to 20 means
			 * that if the following condition holds: Free Connections / MaxConnections <
			 * poolAvailabilityThreshold new connections will be created. In other words, it tries
			 * to keep at least 20% of the pool full of connections. Setting the value to zero will
			 * make the pool create new connections when it needs them but it also means your
			 * application may have to wait for new connections to be obtained at times. Default:
			 * 20.
			 */
			config.setPoolAvailabilityThreshold(20);
			
			/**
			 * If set to true, the pool will not monitor connections for proper closure. Enable this
			 * option if you only ever obtain your connections via a mechanism that is guaranteed to
			 * release the connection back to the pool (eg Spring's jdbcTemplate, some kind of
			 * transaction manager, etc).
			 */
			config.setDisableConnectionTracking(false);
			
			/**
			 * Sets the maximum time (in milliseconds) to wait before a call to getConnection is
			 * timed out. Setting this to zero is similar to setting it to Long.MAX_VALUE Default: 0
			 * ( = wait forever )
			 */
			config.setConnectionTimeoutInMs(0);
			
			/**
			 * Sets the no of ms to wait when close connection watch threads are enabled. 0 = wait
			 * forever.
			 */
			config.setCloseConnectionWatchTimeoutInMs(0);
			
			/**
			 * Sets number of statement helper threads to create that will handle releasing a
			 * statement. When this value is set to zero, the application thread is blocked until
			 * the pool and JDBC driver are able to close off the statement. When a non-zero value
			 * is set, the pool will create threads that will take care of closing off the statement
			 * asychronously to the application via the release helper threads). Useful when your
			 * application is opening up lots of statements otherwise will probably slow things
			 * down.
			 */
			//config.setStatementReleaseHelperThreads(0);
			
			/**
			 * Sets the maxConnectionAge in seconds. Any connections older than this setting will be
			 * closed off whether it is idle or not. Connections currently in use will not be
			 * affected until they are returned to the pool.
			 */
			config.setMaxConnectionAgeInSeconds(0);
			
			
			config.setDefaultAutoCommit(true);
			
			_connectionPool = new BoneCP(config);
		}
		catch(Exception e) {
			log.log(Level.SEVERE, "Failed initializing BoneCP!", e);
			throw new RuntimeException("System critical fail!");
		}
		log.info("Database driver successfully installed");
	}
	
	public void shutdown()
	{
		try
		{
			_connectionPool.close();
			_connectionPool = null;
		}
		catch (Exception e)
		{
			log.fatal("Failed shutting down", e);
		}
	}
	
	public Connection getConnection()
	{
		Connection con = null;
		
		while (con == null)
		{
			try {
				con = _connectionPool.getConnection();
			}
			catch (SQLException e) {
				log.warning("Failed getting a connection, trying again " + e);
			}
		}
		
		return con;
	}
}
