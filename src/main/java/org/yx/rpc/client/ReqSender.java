package org.yx.rpc.client;

import org.apache.mina.core.future.WriteFuture;
import org.yx.exception.SoaException;
import org.yx.rpc.client.route.RouteHolder;
import org.yx.rpc.client.route.WeightedRoute;

public class ReqSender {
	
	/**
	 * 同步发送
	 * @param req
	 * @param timeout
	 * @return
	 * @throws Throwable 
	 */
	public static ReqResp send(Req req,long timeout) throws Throwable{
		String method=req.getMethod();
		WeightedRoute route=RouteHolder.getRoute(method);
		if(route==null){
			SoaException.throwException(2353454, "can not find route for "+method, null);
		}
		ReqSession session=ReqSessionHolder.getSession(route.getUrl());
		RespFuture future=RequestLocker.register(req);
		WriteFuture f=session.write(req);
		if(f.getException()!=null){
			throw f.getException();
		}
		return future.getResponse(timeout);
	}
}
