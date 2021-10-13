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
package org.yx.conf;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.yx.exception.SumkException;
import org.yx.log.RawLog;
import org.yx.util.CollectionUtil;
import org.yx.util.IOUtil;
import org.yx.util.Task;

public class UrlSystemConfig extends MultiNodeConfig {

	protected final List<URL> urls;

	protected long period = 1000L * 60 * 5;
	protected int timeout = 5000;
	protected int readTimeout = 5000;
	protected String method = "GET";

	private Future<?> future;

	public UrlSystemConfig(List<URL> urls) {
		if (urls == null || urls.isEmpty()) {
			throw new SumkException(345212465, "url config的地址为空");
		}
		this.urls = CollectionUtil.unmodifyList(urls);
	}

	public final synchronized void stop() {
		this.started = false;
		this.onStop();
	}

	protected void onStop() {
		Future<?> f = this.future;
		if (f != null) {
			f.cancel(true);
			this.future = null;
		}
	}

	/**
	 * 初始化
	 */
	@Override
	public void init() {
		if (this.future != null) {
			return;
		}
		this.future = Task.scheduleAtFixedRate(this::handle, this.period, this.period, TimeUnit.MILLISECONDS);
	}

	protected void handle() {
		Map<String, String> map = new HashMap<>();
		List<URL> list = new ArrayList<>(urls);
		Collections.reverse(list);
		for (URL url : list) {
			byte[] data = this.extractData(url);
			if (data == null) {
				RawLog.error(LOG_NAME, "data on [" + url + "] is null");
				return;
			}
			if (data.length > 0) {
				map.putAll(this.parse(data));
			}
		}
		if (Objects.equals(this.config, map)) {
			return;
		}
		RawLog.info(LOG_NAME, "data changed");
		this.config = map;
		this.onRefresh();
	}

	protected byte[] extractData(URL url) {
		HttpURLConnection conn = null;
		try {

			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(timeout);
			conn.setRequestMethod(method);
			conn.setReadTimeout(readTimeout);
			conn.setDoOutput(true);
			conn.connect();
			if (conn.getResponseCode() != 200) {
				RawLog.error(LOG_NAME, url + "返回的状态码是" + conn.getResponseCode());
				return null;
			}
			InputStream in = conn.getInputStream();
			return IOUtil.readAllBytes(in, true);
		} catch (Exception e) {
			RawLog.error(LOG_NAME, e);
			return null;
		} finally {
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception e2) {
				}
			}
		}
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		if (this.future != null) {
			throw new SumkException(62654622, "启动后不允许修改刷新间隔");
		}
		this.period = period;
	}

}
