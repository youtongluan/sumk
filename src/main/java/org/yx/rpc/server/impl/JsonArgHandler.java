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

public class JsonArgHandler implements MinaHandler{

	@Override
	public boolean accept(String type) {
		return type.startsWith("${");
	}

	@Override
	public String received(String message) throws Exception {
		long start=System.currentTimeMillis();
		message=message.substring(1);
		String[] msgs=message.split("\r",-1);
		if(msgs.length<2){
			SystemException.throwException(51234, "内容有误");
		}
		Req req=GsonUtil.fromJson(msgs[0], Req.class);
		String sn0=StringUtils.isEmpty(req.getSn0())?req.getSn():req.getSn0();
		SourceSn.register(sn0);
		Response resp=new Response(req.getSn());
		try{
			String method=req.getMethod();
			ActionInfo minfo=ActionHolder.getActionInfo(method);
			req.setArgs(msgs[1]);
			Object ret=minfo.invokeByJsonArg(req.getArgs());
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
