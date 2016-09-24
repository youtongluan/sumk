package org.yx.http.handler;

import org.yx.exception.BizException;
import org.yx.http.ErrorCode;
import org.yx.http.Web;
import org.yx.http.filter.Session;
import org.yx.http.filter.UserSession;
import org.yx.http.start.UserSessionHolder;

public class ReqUserHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return web.requireLogin() || web.requestEncrypt().isAes();
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		String sessionID = ctx.getHeaders().get(Session.SESSIONID);
		UserSession session = UserSessionHolder.userSession();
		byte[] key = session.getkey(sessionID);

		if (key == null) {
			BizException.throwException(ErrorCode.SESSION_ERROR, "请重新登陆");
		}
		ctx.setKey(key);
		session.flushSession();
		return false;
	}

}
