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
package org.yx.http.user;

import java.io.IOException;
import java.util.List;

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

	private LoginServlet[] servs = new LoginServlet[0];

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handle(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handle(req, resp);
	}

	protected void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String type = InnerHttpUtil.getType(req);
		for (LoginServlet s : this.servs) {
			if (s.acceptType(type)) {
				s.service(req, resp);
				return;
			}
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			List<LoginServlet> ss = IOC.getBeans(LoginServlet.class);
			if (ss == null || ss.isEmpty()) {
				Log.get("sumk.http").info("there is no LoginServlet");
				return;
			}
			this.servs = ss.toArray(new LoginServlet[ss.size()]);
			for (LoginServlet serv : ss) {
				serv.init(config);
			}
		} catch (Exception e) {
			Log.get("sumk.http.login").error(e.toString(), e);
		}
	}

}
