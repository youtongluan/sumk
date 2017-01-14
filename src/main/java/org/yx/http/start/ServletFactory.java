/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
