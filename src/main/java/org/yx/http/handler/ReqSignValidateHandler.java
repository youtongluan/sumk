package org.yx.http.handler;

import org.yx.conf.AppInfo;
import org.yx.exception.HttpException;
import org.yx.http.Web;
import org.yx.log.Log;
import org.yx.util.StringUtils;
import org.yx.util.secury.MD5Utils;

/**
 * sign签名校验
 * 
 * @author 游夏
 *
 */
public class ReqSignValidateHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return web.sign();
	}

	private byte[] salt = null;

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
		if (salt == null) {
			String saltStr = AppInfo.get("sumk.sign.salt");
			if (StringUtils.isEmpty(saltStr)) {
				salt = new byte[0];
			} else {
				salt = saltStr.getBytes();
			}
		}
		if (salt.length > 0) {
			byte[] temp = new byte[bs.length + salt.length];
			System.arraycopy(bs, 0, temp, 0, bs.length);
			System.arraycopy(salt, 0, temp, bs.length, salt.length);
			bs = temp;
		}
		String sign1 = MD5Utils.encrypt(bs);
		if (!sign.equals(sign1)) {
			Log.get("sign").debug("client sign:{},computed is:{}", sign, sign1);
			HttpException.throwException(this.getClass(), "签名验证错误");
		}
		return false;
	}

}
