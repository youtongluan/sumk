package org.test.soa.server;

import java.io.File;
import java.io.IOException;

import org.I0Itec.zkclient.ZkServer;
import org.yx.util.UUIDSeed;

public class SOAServer {

	public static void startZKServer() throws IOException{
		String temp=System.getProperty("java.io.tmpdir");
		File f=new File(temp,UUIDSeed.seq());
		f.mkdir();
		ZkServer zk=new ZkServer(f.getAbsolutePath(),f.getAbsolutePath(),zkClient->{});
		zk.start();
	}

}
