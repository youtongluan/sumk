/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.http.ErrorCode;
import org.yx.http.HttpSessionHolder;
import org.yx.http.Web;
import org.yx.http.filter.UserSession;
import org.yx.log.Log;
import org.yx.util.StringUtils;

public class ReqUserHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return web.requireLogin() || web.requestEncrypt().isAes();
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		String sessionID = ctx.getHeaders().get(UserSession.SESSIONID);
		UserSession session = HttpSessionHolder.loadUserSession();
		byte[] key = session.getKey(sessionID);

		if (key == null) {
			if (HttpSessionHolder.isSingleLogin()) {
				String userId = ctx.getHeaders().get(UserSession.TOKEN);
				if (StringUtils.isNotEmpty(userId)) {

					if (session.isLogin(userId)) {
						int code = AppInfo.getInt("http.session.single.code", ErrorCode.LOGIN_AGAIN);
						String msg = AppInfo.get("http.session.single.msg", "您已在其他地方登录！");
						Log.get("sumk.http").info("session:{}, login by other", sessionID);
						BizException.throwException(code, msg);
					}
				}
			}
			Log.get("sumk.http").info("session:{}, has expired", sessionID);
			BizException.throwException(ErrorCode.SESSION_ERROR, "请重新登陆");
		}
		ctx.setKey(key);
		session.flushSession();
		return false;
	}

}
