package org.yx.rpc.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.yx.log.Log;
import org.yx.util.GsonUtil;

public class ReqSession {
	
	protected IoSession session;
	private NioSocketConnector connector;
	private InetSocketAddress addr;
	private void ensureSession(){
		if(session!=null&&!session.isClosing()){
			return;
		}
		synchronized(this){
			if(connector==null||connector.isDisposing()||connector.isDisposed()){
				Log.get("SYS.6").debug("create connector for {}",addr);
				connector = new NioSocketConnector();
				connector.setConnectTimeoutMillis(5000);
				connector.setHandler(new ClientHandler());
				connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),"\n","\n")));
			}
			
			if(session==null||session.isClosing())
			{
				Log.get("SYS.7").debug("create session for {}",addr);
				ConnectFuture cf=connector.connect(addr);
				
				cf.awaitUninterruptibly(connector.getConnectTimeoutMillis()+1);
				IoSession se=cf.getSession();
				if(se!=null){
					this.session=se;
					return;
				}
				cf.cancel();
				throw new RuntimeException("创建连接失败");
			}
		}
	}
	public ReqSession(String ip,int port){
		addr=new InetSocketAddress(ip,port);
	}
	public WriteFuture write(Req req){
		this.ensureSession();
		String args=req.getArgs();
		
		if(args!=null){
			Log.get("SYS.8").trace("args:{}",args);
			req.setArgs(null);
			req.setParams(null);
			String json_req="$"+GsonUtil.toJson(req)+"\r"+args;
			return this.session.write(json_req);
		}
		
		String[] params=req.getParams();
		req.setParams(null);
		String pre=String.format("%02d", params.length);
		String json_req=pre+GsonUtil.toJson(req);
		for(String p:params){
			json_req+="\r"+p;
		}
		return this.session.write(json_req);
	}
	
	public void close(){
		this.session.close(true);
	}
}
