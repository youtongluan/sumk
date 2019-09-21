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
package org.yx.http;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.annotation.Bean;
import org.yx.annotation.http.SumkServlet;
import org.yx.main.SumkThreadPool;

@Bean
@SumkServlet(value = { "/_thread_info" }, loadOnStartup = -1)
public class ThreadInfoAction extends HttpServlet {

	private static final long serialVersionUID = 2364534491L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ThreadPoolExecutor pool = (ThreadPoolExecutor) SumkThreadPool.executor();
		String line = req.getParameter("line");
		if (line == null || line.isEmpty()) {
			line = "\n";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("active thread:").append(pool.getActiveCount());
		sb.append(", thread count:" + pool.getPoolSize());
		sb.append(", queue:" + pool.getQueue().size());
		sb.append(line);
		sb.append("only for current threads:: commited task:" + pool.getTaskCount());
		sb.append(", completed task:" + pool.getCompletedTaskCount());
		if ("1".equals(req.getParameter("full"))) {

			sb.append(line);
			sb.append("max thread:").append(pool.getMaximumPoolSize());
			sb.append(", idle timeout(ms):").append(pool.getKeepAliveTime(TimeUnit.MILLISECONDS));
		}
		resp.getWriter().write(sb.toString());
	}
}
