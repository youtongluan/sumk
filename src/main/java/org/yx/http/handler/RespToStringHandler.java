package org.yx.http.handler;

import org.yx.http.Web;
import org.yx.util.GsonUtil;

public class RespToStringHandler implements HttpHandler{

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Throwable {
		Object obj=ctx.getResult();
		if(obj==null){
			ctx.setResult(GsonUtil.toJson(obj));
			return false;
		}
		Class<?> clz=obj.getClass();
		if(clz.isArray()){
			ctx.setResult(GsonUtil.toJson(obj));
			return false;
		}
		if(clz.isPrimitive()||clz.equals(String.class)){
			ctx.setResult(String.valueOf(obj));
			return false;
		}
		ctx.setResult(GsonUtil.toJson(obj));
		return false;
	}

}
