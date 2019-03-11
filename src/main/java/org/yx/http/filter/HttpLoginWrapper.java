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
package org.yx.http.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.bean.IOC;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.log.Log;

public class HttpLoginWrapper extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Map<String, LoginServlet> servs = new HashMap<>();

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String type = InnerHttpUtil.getType(req);
		LoginServlet s = servs.get(type);
		if (s != null) {
			s.service(req, resp);
			return;
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			List<LoginServlet> ss = IOC.getBeans(LoginServlet.class);
			if (ss == null) {
				Log.get("sumk.http").info("there is no LoginServlet");
				return;
			}
			for (LoginServlet serv : ss) {
				String type = serv.getType();
				if (type == null) {
					type = "";
				}
				servs.put(type, serv);
				serv.init(config);
			}
		} catch (Exception e) {
			Log.printStack(e);
		}
	}

}
