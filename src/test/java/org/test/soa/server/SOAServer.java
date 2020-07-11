package org.test.soa.server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.yx.util.UUIDSeed;

public class SOAServer {

	private static ZooKeeperServer zk;
	private static NIOServerCnxnFactory nioFactory;
	public static void startZKServer() throws IOException, InterruptedException{
		startZKServer(500,2000);
	}
	
	public static void stopZKServer(){
		if(zk!=null){
			zk.shutdown();
		}
	}
	
	public static void startZKServer(int tickTime, int minSessionTimeout) throws IOException, InterruptedException{
		String temp=System.getProperty("java.io.tmpdir");
		File f=new File(temp,UUIDSeed.seq());
		f.mkdir();
		
		
		zk = new ZooKeeperServer(f, f, tickTime);
        zk.setMinSessionTimeout(minSessionTimeout);
        nioFactory = new NIOServerCnxnFactory();
        int maxClientConnections = 0; // 0 means unlimited
        nioFactory.configure(new InetSocketAddress("127.0.0.1",2181), maxClientConnections);
        nioFactory.startup(zk);
	}
}
