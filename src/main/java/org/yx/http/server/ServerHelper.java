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

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.conf.AppInfo;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.log.Logs;
import org.yx.util.S;

public final class ServerHelper {

	public static boolean preHandle(HttpServletRequest req, HttpServletResponse resp, String firstKey) {
		InnerHttpUtil.noCache(resp);
		resp.setContentType("text/plain;charset=UTF-8");
		String md5 = AppInfo.get(firstKey, "sumk.union.monitor", "61c72b1ce5858d83c90ba7b5b1096697");
		String sign = req.getParameter("sign");
		if (sign == null) {
			Logs.http().debug("sign is empty");
			return false;
		}
		try {
			String signed = S.hash().digest(sign, StandardCharsets.UTF_8);
			if (!md5.equalsIgnoreCase(signed)) {
				Logs.http().debug("signed:{},need:{}", signed, md5);
				return false;
			}
		} catch (Exception e) {
		}
		return true;
	}
}
