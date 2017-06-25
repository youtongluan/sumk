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
package org.yx.http.handler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.bean.Bean;
import org.yx.common.ActStatis;
import org.yx.common.Statis;
import org.yx.conf.AppInfo;
import org.yx.http.HttpHolder;
import org.yx.http.SumkServlet;
import org.yx.util.StringUtils;
import org.yx.util.secury.MD5Utils;

@Bean
@SumkServlet(value = { "/sumk_statis" }, loadOnStartup = -1)
public class HttpStatis extends HttpServlet {

	private static final long serialVersionUID = 2364534491L;

	private String getServerInfo() {
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

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String md5 = AppInfo.get("http.monitor", "61C72B1CE5858D83C90BA7B5B1096697");
		String sign = req.getParameter("sign");
		String mode = req.getParameter("mode");
		try {
			if (sign == null || !md5.equals(MD5Utils.encrypt(sign.getBytes())) || StringUtils.isEmpty(mode)) {
				return;
			}
		} catch (Exception e) {
		}
		if (mode.equals("serverInfo")) {
			resp.getWriter().write(getServerInfo());
			return;
		}
		if (mode.equals("acts")) {
			resp.getWriter().write(Arrays.toString(HttpHolder.acts()));
			return;
		}
		if (mode.equals("statis")) {
			ActStatis actStatic = HttpHandlerChain.actStatic;
			Map<String, Statis> map = actStatic.getAll();
			Collection<Statis> values = map.values();
			for (Statis v : values) {
				resp.getWriter().write(v.toString());
				resp.getWriter().write("\n");
			}
			return;
		}

	}
}
