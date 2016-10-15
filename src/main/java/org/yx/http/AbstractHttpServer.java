package org.yx.http;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.log.Log;

/**
 * 
 * @author Administrator
 */
public abstract class AbstractHttpServer extends HttpServlet {

	private static final long serialVersionUID = 74378082364534491L;

	public String getServerInfo() {
		long now = System.currentTimeMillis();
		long ms = now - startTime;
		StringBuilder sb = new StringBuilder();
		String ln = "\n";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		sb.append("start at:" + sdf.format(new Date(startTime)));
		sb.append(ln);
		sb.append("run(ms):" + ms);
		return sb.toString();
	}

	private long startTime = System.currentTimeMillis();

	private boolean validPath(Class<?> actionClz, String path) {
		String pname = actionClz.getName();
		String[] names = pname.split("\\.");
		for (int i = 2; i < names.length; i++) {
			if (names[i].equals(path)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) {
		try {

			setRespHeader(req, resp);

			final String act = req.getParameter("act");
			String path = req.getPathInfo();
			Log.get(this.getClass()).trace("{}?act={}", path, act);
			if (path.endsWith("/")) {
				path = path.substring(0, path.length() - 1);
			}
			int index = path.lastIndexOf("/");
			if (index < 0) {
				Log.get(this.getClass()).error(path + " path error");
				HttpUtil.error(resp, -1001, "请求格式不正确", HttpUtil.charset(req));
				return;
			}
			path = path.substring(index + 1);
			if (act == null) {
				Log.get(this.getClass()).error("act is empty");
				HttpUtil.error(resp, -1002, "请求格式不正确", HttpUtil.charset(req));
				return;
			}
			HttpInfo info = HttpHolder.getHttpInfo(act);
			if (info == null) {
				Log.get(this.getClass()).error(act + " donot found handler");
				HttpUtil.error(resp, -1003, "请求格式不正确", HttpUtil.charset(req));
				return;
			}
			if (!validPath(info.getObj().getClass(), path)) {
				Log.get(this.getClass()).error(act + " in error package");
				HttpUtil.error(resp, -1004, "请求的模块不正确", HttpUtil.charset(req));
				return;
			}
			HttpHeadersHolder.setHttpRequest(req);
			handle(act, info, req, resp);

		} catch (Exception e) {
			Log.printStack(e);
			try {
				HttpUtil.error(resp, -1005, "请求格式不正确", HttpUtil.charset(req));
			} catch (IOException e1) {
				Log.printStack(e);
			}
		} finally {
		}
	}

	protected void setRespHeader(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json;charset=" + HttpUtil.charset(req));
	}

	protected abstract void handle(String act, HttpInfo info, HttpServletRequest req, HttpServletResponse resp)
			throws Exception;;
}
