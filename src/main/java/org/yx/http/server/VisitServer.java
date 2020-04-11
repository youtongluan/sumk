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
package org.yx.http.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.annotation.Bean;
import org.yx.annotation.http.SumkServlet;
import org.yx.conf.AppInfo;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.PojoMetaHolder;
import org.yx.db.sql.VisitCounter;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.log.Logs;
import org.yx.util.S;

@Bean
@SumkServlet(value = { "/_sumk_cache_monitor" }, loadOnStartup = -1, appKey = "sumkCacheMonitor")
public class VisitServer extends AbstractCommonHttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String BLANK = "  ";

	@Override
	protected void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		InnerHttpUtil.noCache(resp);
		resp.setContentType("text/plain;charset=UTF-8");
		String md5 = AppInfo.get("sumk.http.cache.monitor", "sumk.union.monitor", "61c72b1ce5858d83c90ba7b5b1096697");
		String sign = req.getParameter("sign");
		if (sign == null) {
			Logs.http().debug("sign is empty");
			return;
		}
		try {
			String signed = S.hash().digest(sign, StandardCharsets.UTF_8);
			if (!md5.equalsIgnoreCase(signed)) {
				Logs.http().debug("signed:{},need:{}", signed, md5);
				return;
			}
		} catch (Exception e) {
		}
		resp.getOutputStream().write(visitInfo().getBytes(StandardCharsets.UTF_8));
	}

	public static String visitInfo() {
		List<PojoMeta> list = PojoMetaHolder.allPojoMeta();
		StringBuilder sb = new StringBuilder();
		sb.append("##tableName").append(BLANK).append("visitCount").append(BLANK).append("cachedMeeted")
				.append(AppInfo.LN);
		for (PojoMeta p : list) {
			VisitCounter c = p.getCounter();
			sb.append(p.getTableName()).append(BLANK).append(c.getVisitCount()).append(BLANK).append(c.getCachedMeet())
					.append(AppInfo.LN);
		}
		return sb.toString();
	}

}
