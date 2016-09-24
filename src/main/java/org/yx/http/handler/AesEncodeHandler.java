package org.yx.http.handler;

import org.yx.http.Web;
import org.yx.util.secury.EncryUtil;

/**
 * base64解码
 * 
 * @author youtl
 *
 */
public class AesEncodeHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return web.responseEncrypt().isAes();
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		byte[] bs = (byte[]) ctx.getResult();
		byte[] data = EncryUtil.encrypt(bs, ctx.getKey());
		ctx.setResult(data);
		return false;
	}

}
