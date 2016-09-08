package org.yx.http.handler;

import org.yx.http.Web;

public class RespBodyHandler implements HttpHandler{

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Throwable {
		byte[] data=(byte[]) ctx.getResult();
		ctx.getHttpResponse().getOutputStream().write(data);
		return true;
	}

}
