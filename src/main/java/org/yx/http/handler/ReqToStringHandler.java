package org.yx.http.handler;

import org.yx.http.Web;

public class ReqToStringHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		byte[] bs = (byte[]) ctx.getData();
		if (bs == null) {
			ctx.setData("");
			return false;
		}
		String charset = ctx.getCharset();
		String data = new String(bs, charset);
		ctx.setData(data);
		return false;
	}

}
