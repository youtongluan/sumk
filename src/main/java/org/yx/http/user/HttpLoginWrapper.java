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

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.bean.IOC;
import org.yx.http.server.AbstractCommonHttpServlet;
import org.yx.log.Logs;

public class HttpLoginWrapper extends AbstractCommonHttpServlet {

	private static final long serialVersionUID = 1L;

	private LoginServlet serv;

	protected void handle(HttpServletRequest req, HttpServletResponse resp) {
		serv.service(req, resp);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			List<LoginServlet> ss = IOC.getBeans(LoginServlet.class);
			if (ss == null || ss.isEmpty()) {
				Logs.http().info("there is no LoginServlet");
				return;
			}
			if (ss.size() > 1) {
				Logs.http().warn("there is {} login servlet", ss.size());
			}
			this.serv = ss.get(0);
			serv.init(config);
		} catch (Exception e) {
			Logs.http().error(e.getLocalizedMessage(), e);
		}
	}

}
