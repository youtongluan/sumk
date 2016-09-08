package org.yx.http.handler;

import org.yx.http.Web;

public interface HttpHandler {
	boolean accept(Web web);
	/**
	 * 
	 * @param request
	 * @return true表示处理完毕，false表示需要继续处理
	 */
	boolean handle(WebContext ctx) throws Throwable;
}
