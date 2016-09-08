package org.yx.http.handler;

import javax.servlet.http.HttpServletResponse;

import org.yx.http.Web;
import org.yx.http.filter.Session;
/**
 * 用来写入内容主题，是最后一个handler
 * @author youtl
 *
 */
public class RespHeaderHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Throwable {
		HttpServletResponse resp=ctx.getHttpResponse();
		resp.setCharacterEncoding(ctx.getCharset());
		String sessionID=ctx.getHeaders().get(Session.SESSIONID);
		if(sessionID!=null&&sessionID.length()>0){
			resp.setHeader(Session.SESSIONID, sessionID);
		}
		return false;
	}

}
