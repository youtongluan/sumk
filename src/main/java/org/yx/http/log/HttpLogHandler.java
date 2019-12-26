package org.yx.http.log;

import javax.servlet.http.HttpServletRequest;

import org.yx.http.handler.WebContext;

/**
 * 这个在用户线程里执行，可以取到线程变量。处理最好用异步，要不然会阻塞请求，影响用户体验
 */
public interface HttpLogHandler {

	void log(WebContext ctx, HttpServletRequest req, Throwable ex, long time);
}
