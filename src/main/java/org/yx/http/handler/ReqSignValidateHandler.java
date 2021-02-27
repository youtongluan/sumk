/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.http.handler;

import org.yx.annotation.Bean;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.http.HttpErrorCode;
import org.yx.http.kit.HttpCiphers;
import org.yx.log.Logs;
import org.yx.util.StringUtil;

@Bean
public class ReqSignValidateHandler implements HttpHandler {

	private byte[] salt;

	@Override
	public int order() {
		return 1600;
	}

	public ReqSignValidateHandler() {

		String saltStr = AppInfo.get("sumk.http.sign.salt");
		if (StringUtil.isNotEmpty(saltStr)) {
			salt = saltStr.getBytes();
		}
	}

	@Override
	public void handle(WebContext ctx) throws Exception {
		if (!ctx.node().sign()) {
			return;
		}
		byte[] bs = ctx.getDataInByteArray();
		if (bs == null) {
			return;
		}
		String sign = ctx.sign();
		if (StringUtil.isEmpty(sign)) {
			throw BizException.create(HttpErrorCode.SIGN_EMPTY, "签名不能为空");
		}
		if (salt != null) {
			byte[] temp = new byte[bs.length + salt.length];
			System.arraycopy(bs, 0, temp, 0, bs.length);
			System.arraycopy(salt, 0, temp, bs.length, salt.length);
			bs = temp;
		}
		String sign1 = HttpCiphers.getSigner().sign(bs, ctx.httpRequest());
		if (!sign.equals(sign1)) {
			Logs.http().debug("client sign:{},computed is:{}", sign, sign1);
			throw BizException.create(HttpErrorCode.SIGN_MISTAKE, "签名验证错误");
		}
	}

}
