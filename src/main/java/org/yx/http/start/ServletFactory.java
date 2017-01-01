package org.yx.http.start;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;

import org.yx.common.StartContext;
import org.yx.http.ServletInfo;
import org.yx.http.SumkServlet;
import org.yx.util.StringUtils;

class ServletFactory {

	@SuppressWarnings("unchecked")
	public void resolve(Class<?> clz) throws Exception {
		if (!Servlet.class.isAssignableFrom(clz)) {
			return;
		}
		SumkServlet ws = clz.getAnnotation(SumkServlet.class);
		if (ws == null) {
			return;
		}
		String[] names = ws.value();
		if (names == null) {
			return;
		}
		for (String name : names) {
			if (StringUtils.isEmpty(name)) {
				name = "/" + clz.getSimpleName().toLowerCase();
			}
			List<ServletInfo> servlets = (List<ServletInfo>) StartContext.inst.getOrCreate(ServletInfo.class,
					new ArrayList<ServletInfo>());
			servlets.add(new ServletInfo().setServletClz((Class<? extends Servlet>) clz).setPath(name));
		}

	}

}
