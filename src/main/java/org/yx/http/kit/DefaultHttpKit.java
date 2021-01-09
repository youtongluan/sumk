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
package org.yx.http.kit;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.common.action.ActStatis;
import org.yx.common.action.ActStatisImpl;
import org.yx.http.HttpJson;
import org.yx.log.Logs;
import org.yx.util.StringUtil;

public class DefaultHttpKit implements HttpKit {

	private static final int MAX_EXPECT_SIZE = 1024 * 1024;
	private static final int MIN_EXPECT_SIZE = 64;

	protected final ActStatis actStatis;

	public DefaultHttpKit() {
		this.actStatis = new ActStatisImpl();
	}

	public DefaultHttpKit(ActStatis actStatis) {
		this.actStatis = Objects.requireNonNull(actStatis);
	}

	public Charset charset(HttpServletRequest req) {
		String charsetName = req.getCharacterEncoding();
		if (StringUtil.isEmpty(charsetName)) {
			return HttpSettings.defaultCharset();
		}
		if ("UTF8".equalsIgnoreCase(charsetName) || "UTF-8".equalsIgnoreCase(charsetName)) {
			return StandardCharsets.UTF_8;
		}
		if (!Charset.isSupported(charsetName)) {
			Logs.http().warn("charset '{}' is not supported,use default charset {}", charsetName,
					HttpSettings.defaultCharset());
			return HttpSettings.defaultCharset();
		}
		return Charset.forName(charsetName);
	}

	@Override
	public void sendError(HttpServletResponse resp, int code, String errorMsg, Charset charset) throws IOException {
		resp.setStatus(HttpSettings.errorHttpStatus());
		Map<String, Object> map = new LinkedHashMap<>(2, 1);
		map.put("code", code);
		map.put("message", errorMsg);
		resp.getOutputStream().write(HttpJson.operator().toJson(map).getBytes(charset));
	}

	public void noCache(HttpServletResponse resp) {
		resp.setHeader("cache-control", "no-store");
		resp.setDateHeader("Expires", 0);
	}

	@Override
	public void record(String act, long time, boolean isSuccess) {
		actStatis.visit(act, time, isSuccess);
	}

	@Override
	public ActStatis actStatis() {
		return this.actStatis;
	}

	@Override
	public void setRespHeader(HttpServletResponse resp, Charset charset) throws IOException {
		resp.setContentType("application/json;charset=" + charset.name());
	}

	@Override
	public int expectReqDataSize(int expect) {
		if (expect < 0) {
			return 1024;
		}
		if (expect < MIN_EXPECT_SIZE) {
			return MIN_EXPECT_SIZE;
		}
		if (expect > MAX_EXPECT_SIZE) {
			return MAX_EXPECT_SIZE;
		}
		return expect;
	}
}
