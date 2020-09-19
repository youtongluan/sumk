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
import org.yx.annotation.http.Web;
import org.yx.common.context.ActionContext;
import org.yx.http.HttpContextHolder;
import org.yx.http.HttpErrorCode;
import org.yx.http.kit.HttpException;
import org.yx.http.kit.HttpSettings;
import org.yx.http.user.SessionObject;
import org.yx.http.user.UserSession;
import org.yx.http.user.WebSessions;
import org.yx.log.Logs;
import org.yx.util.StringUtil;
import org.yx.util.UUIDSeed;

@Bean
public class ReqUserHandler implements HttpHandler {

	@Override
	public int order() {
		return 1000;
	}

	private boolean accept(Web web) {
		return web.requireLogin() || web.requestType().isEncrypt();
	}

	@Override
	public void handle(WebContext ctx) throws Exception {
		if (!this.accept(ctx.web())) {
			return;
		}
		String sessionId = HttpContextHolder.sessionId();
		if (!WebSessions.getSessionIdVerifier().test(sessionId)) {
			Logs.http().info("session:{}, is not valid", sessionId);
			throw HttpException.create(HttpErrorCode.SESSION_ERROR, "token无效");
		}
		UserSession session = WebSessions.loadUserSession();
		SessionObject obj = session.getUserObject(sessionId, SessionObject.class);

		if (obj == null) {
			if (HttpSettings.isSingleLogin()) {
				String userId = HttpContextHolder.userFlag();
				if (StringUtil.isNotEmpty(userId)) {

					if (session.isLogin(userId)) {
						Logs.http().info("session:{}, login by other", sessionId);
						throw HttpException.create(HttpErrorCode.LOGIN_AGAIN, "您已在其他地方登录！");
					}
				}
			}
			Logs.http().info("session:{}, has expired", sessionId);
			throw HttpException.create(HttpErrorCode.SESSION_ERROR, "请重新登录");
		}
		ActionContext.get().userId(obj.getUserId());
		ActionContext.get().setTraceIdIfAbsent(UUIDSeed.seq18());
	}

}
