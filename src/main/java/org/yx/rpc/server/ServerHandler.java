package org.yx.rpc.server;

import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.yx.log.Log;


public class ServerHandler extends IoHandlerAdapter {
    private List<MinaHandler> handlers;
    
    public ServerHandler(List<MinaHandler> handlers) {
		super();
		this.handlers = handlers;
	}
	public void addHandler(MinaHandler h){
    	this.handlers.add(h);
    }
    @Override
    public void sessionCreated(IoSession session) {
    	Log.get("SYS.51").debug("create session:{}",session.getId());
    }

    
    @Override
    public void sessionClosed(IoSession session) throws Exception {



    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
    	if(session.getIdleCount(status)>10000){
    		Log.get("SYS.52").debug("session:{}, idle:{},will close",session.getId(),session.getIdleCount(status));
    		session.close(true);
    	}
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
    	Log.get("SYS.53").error(session.getId()+":"+cause.getMessage(),cause);
        session.close(true);
    }
    
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        String msg=(String) message;
        Log.get("SYS.54").trace( msg );
        for(MinaHandler h:handlers){
        	if(h.accept(msg)){
        		String ret=h.received(msg);
        		session.write(ret);
        		return;
        	}
        }
    }
}