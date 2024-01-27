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
import org.yx.base.context.ActionContext;
import org.yx.conf.AppInfo;
import org.yx.exception.BizException;
import org.yx.http.HttpErrorCode;
import org.yx.http.WebUtil;
import org.yx.http.kit.HttpSettings;
import org.yx.http.user.SessionObject;
import org.yx.http.user.UserSession;
import org.yx.http.user.WebSessions;
import org.yx.log.Logs;
import org.yx.util.StringUtil;

@Bean
public class ReqUserHandler implements HttpHandler {

	private boolean tokenMode = AppInfo.getBoolean("sumk.http.session.token", false);

	@Override
	public int order() {
		return 1200;
	}

	@Override
	public void handle(WebContext ctx) throws Exception {
		if (ctx.node().requireLogin()) {
			checkSession(WebUtil.getSessionId(), WebUtil.getUserFlag(ctx.httpRequest()));
		}
	}

	public void checkSession(String sessionId, String userId) {
		if (!WebSessions.getSessionIdVerifier().test(sessionId)) {
			Logs.http().warn("sessionId:{}, is not valid", sessionId);
			throw BizException.create(HttpErrorCode.SESSION_ERROR, "token无效");
		}
		UserSession session = WebSessions.loadUserSession();

		SessionObject obj = tokenMode ? session.getUserObject(sessionId, SessionObject.class)
				: session.loadAndRefresh(sessionId, SessionObject.class);

		if (obj == null) {

			if (HttpSettings.isSingleLogin() && StringUtil.isNotEmpty(userId) && session.sessionId(userId) != null) {
				Logs.http().info("sessionId:{}, login by other place", sessionId);
				throw BizException.create(HttpErrorCode.LOGIN_AGAIN, "您已在其他地方登录！");
			}
			Logs.http().info("sessionId:{}, 没找到对应的session", sessionId);
			throw BizException.create(HttpErrorCode.SESSION_ERROR, "请重新登录");
		}
		ActionContext.current().userId(obj.getUserId());

		Long deadTime = obj.getExpiredTime();
		if (deadTime != null) {
			if (deadTime < System.currentTimeMillis()) {
				Logs.http().warn("sessionId:{}, expiredTime:{}，使用时间太长", sessionId, deadTime);
				throw BizException.create(HttpErrorCode.SESSION_ERROR, "session使用时间太长");
			}
		}
	}

}
