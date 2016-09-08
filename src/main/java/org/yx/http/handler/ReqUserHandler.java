package org.yx.http.handler;


import org.yx.exception.BizException;
import org.yx.http.ErrorCode;
import org.yx.http.Web;
import org.yx.http.filter.Session;
import org.yx.http.filter.SessionFilter;

public class ReqUserHandler implements HttpHandler{

	private SessionFilter sessionFilter;
	
	public SessionFilter getSessionFilter() {
		return sessionFilter;
	}

	public void setSessionFilter(SessionFilter sessionFilter) {
		this.sessionFilter = sessionFilter;
	}
	


	public ReqUserHandler(SessionFilter sessionFilter) {
		super();
		this.sessionFilter = sessionFilter;
	}

	@Override
	public boolean accept(Web web) {
		return web.requireLogin()||
				web.requestEncrypt().isAes();
	}

	@Override
	public boolean handle(WebContext ctx) throws Exception {
		String sessionID=ctx.getHeaders().get(Session.SESSIONID);
		byte[] key=this.sessionFilter.getkey(sessionID);
		
		if(key==null){
			BizException.throwException(ErrorCode.SESSION_ERROR,  "请重新登陆");
		}
		ctx.setKey(key);
		return false;
	}

}
