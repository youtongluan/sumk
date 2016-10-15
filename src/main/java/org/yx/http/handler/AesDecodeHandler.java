package org.yx.http.handler;

import org.yx.http.Web;
import org.yx.util.secury.EncryUtil;

/**
 * base64解码
 * 
 * @author youtl
 *
 */
public class AesDecodeHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return web.requestEncrypt().isAes();
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		if (ctx.getInfo().getArgClz() == null) {
			return false;
		}
		byte[] bs = (byte[]) ctx.getData();
		byte[] key = ctx.getKey();
		byte[] data = EncryUtil.decrypt(bs, key);
		ctx.setData(data);
		return false;
	}

}
