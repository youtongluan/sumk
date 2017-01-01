package org.yx.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.http.handler.HttpHandlerChain;
import org.yx.http.handler.WebContext;
import org.yx.log.Log;

/**
 * 
 * @author Administrator
 */
@SumkServlet(value = { "/webserver/*" }, loadOnStartup = -1)
public class WebServer extends AbstractHttpServer {

	private static final long serialVersionUID = 74378082364534491L;

	@Override
	protected void handle(String act, HttpInfo info, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		if (info.getUpload() != null) {
			Log.get(this.getClass()).error(act + " type error.It is not uploader");
			return;
		}

		WebContext wc = new WebContext(act, info, req, resp);
		HttpHandlerChain.inst.handle(wc);
	}
}
