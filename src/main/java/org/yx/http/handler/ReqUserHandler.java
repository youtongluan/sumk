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

import org.yx.annotation.http.Web;
import org.yx.common.context.ActionContext;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.http.HttpContextHolder;
import org.yx.http.HttpErrorCode;
import org.yx.http.user.SessionObject;
import org.yx.http.user.UserSession;
import org.yx.http.user.WebSessions;
import org.yx.log.Log;
import org.yx.util.StringUtil;
import org.yx.util.UUIDSeed;

public class ReqUserHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return web.requireLogin() || web.requestEncrypt().isAes();
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		String sessionId = HttpContextHolder.sessionId();
		UserSession session = WebSessions.loadUserSession();
		byte[] key = session.getKey(sessionId);
		SessionObject obj = session.getUserObject(sessionId, SessionObject.class);

		if (key == null || obj == null) {
			String type = HttpContextHolder.getType();
			if (WebSessions.isSingleLogin(type)) {
				String userId = HttpContextHolder.getToken();
				if (StringUtil.isNotEmpty(userId)) {

					if (session.isLogin(userId)) {
						int code = AppInfo.getInt("sumk.http.session.single.code", HttpErrorCode.LOGIN_AGAIN);
						String msg = AppInfo.get("sumk.http.session.single.msg", "您已在其他地方登录！");
						Log.get("sumk.http").info("session:{}, login by other", sessionId);
						BizException.throwException(code, msg);
					}
				}
			}
			Log.get("sumk.http").info("session:{}, has expired", sessionId);
			BizException.throwException(HttpErrorCode.SESSION_ERROR, "请重新登陆");
		}
		ctx.key(key);
		ActionContext.get().userId(obj.getUserId());
		ActionContext.get().setTraceIdIfAbsent(UUIDSeed.seq18());
		return false;
	}

}
