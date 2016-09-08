package org.yx.rpc.server.impl;

import org.springframework.util.StringUtils;
import org.yx.exception.SoaException;
import org.yx.exception.SystemException;
import org.yx.rpc.ActionHolder;
import org.yx.rpc.ActionInfo;
import org.yx.rpc.SourceSn;
import org.yx.rpc.client.Req;
import org.yx.rpc.server.MinaHandler;
import org.yx.rpc.server.Response;
import org.yx.util.GsonUtil;

public class DefaultMinaHandler implements MinaHandler{

	@Override
	public boolean accept(String type) {
		return type.length()>10&&type.charAt(2)=='{';
	}

	@Override
	public String received(String message) throws Exception {
		long start=System.currentTimeMillis();
		String argLength=message.substring(0, 2);
		message=message.substring(2);
		String[] msgs=message.split("\r",-1);
		if(msgs.length<2){
			SystemException.throwException(51234, "内容有误");
		}
		Req req=GsonUtil.fromJson(msgs[0], Req.class);
		String sn0=StringUtils.isEmpty(req.getSn0())?req.getSn():req.getSn0();
		SourceSn.register(sn0);
		Response resp=new Response(req.getSn());
		try{
			int len=Integer.parseInt(argLength);
			if(len>0){
				String[] params=new String[len];
				for(int i=0;i<len;i++){
					params[i]=msgs[i+1];
				}
				req.setParams(params);
			}
			String method=req.getMethod();
			ActionInfo minfo=ActionHolder.getActionInfo(method);
			Object ret=minfo.invokeByOrder(req.getParams());
			resp.setJson(GsonUtil.toJson(ret));
			resp.setException(null);
			resp.setMs(System.currentTimeMillis()-start);
		}catch(Throwable e){
			resp.setJson(null);
			resp.setException(new SoaException(1001,e.getMessage(),e));
			resp.setMs(System.currentTimeMillis()-start);
		}finally{
			SourceSn.removeSn0();
		}
		return GsonUtil.toJson(resp);
	}
	
}
