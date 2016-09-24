package org.yx.http.handler;

import org.apache.commons.codec.binary.Base64;
import org.yx.http.Web;

/**
 * base64解码
 * 
 * @author youtl
 *
 */
public class Base64DecodeHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return web.requestEncrypt().isBase64();
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		byte[] bs;
		if (String.class.isInstance(ctx.getData())) {
			bs = ((String) ctx.getData()).getBytes(ctx.getCharset());
		} else {
			bs = (byte[]) ctx.getData();
		}
		byte[] data = Base64.decodeBase64(bs);
		ctx.setData(data);
		return false;
	}

}
