package org.yx.http.handler;

import org.springframework.util.StringUtils;
import org.yx.exception.HttpException;
import org.yx.http.Web;
import org.yx.util.secury.MD5Utils;

/**
 * sign签名校验
 * 
 * @author youtl
 *
 */
public class SignValidateHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return web.sign();
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		String sign = ctx.getSign();
		if (StringUtils.isEmpty(sign)) {
			HttpException.throwException(this.getClass(), "签名不能为空");
		}
		byte[] bs;
		if (String.class.isInstance(ctx.getData())) {
			bs = ((String) ctx.getData()).getBytes(ctx.getCharset());
		} else {
			bs = (byte[]) ctx.getData();
		}
		String sign1 = MD5Utils.encrypt(bs);
		if (!sign.equals(sign1)) {
			HttpException.throwException(this.getClass(), "签名验证错误");
		}
		return false;
	}

}
