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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.yx.log.ConsoleLog;
import org.yx.log.SimpleLoggerHolder;
import org.yx.main.SumkServer;
import org.yx.util.StreamUtil;

public abstract class AbstractUrlConfig {

	protected final URL url;

	public AbstractUrlConfig(URL url) {
		this.url = url;
	}

	protected long period = 1000L * 60 * 5;
	protected int timeout = 5000;
	protected int readTimeout = 5000;
	protected String method = "GET";
	protected Charset charset = StandardCharsets.UTF_8;
	protected byte[] rawData;

	private volatile boolean threadStarted;

	protected volatile boolean stoped;

	public void stop() {
		this.stoped = true;
	}

	/**
	 * 初始化
	 */
	public void init() {
		extractData();
		if (threadStarted) {
			return;
		}
		new Thread(() -> {
			synchronized (AbstractUrlConfig.this) {
				if (threadStarted) {
					return;
				}
				threadStarted = true;
			}
			while (!SumkServer.isDestoryed() && !stoped) {
				try {
					Thread.sleep(period);
				} catch (InterruptedException e) {
					ConsoleLog.get("sumk.conf.url").debug("url config exited because interrupted");
					return;
				}
				if (extractData()) {
					notifyUpdate();
				}
			}
		}).start();
	}

	protected abstract void notifyUpdate();

	protected boolean extractData() {
		HttpURLConnection conn = null;
		try {

			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(timeout);
			conn.setRequestMethod(method);
			conn.setReadTimeout(readTimeout);
			conn.setDoOutput(true);
			conn.connect();
			if (conn.getResponseCode() != 200) {
				return false;
			}
			InputStream in = conn.getInputStream();
			byte[] data = StreamUtil.extractData(in, true);
			if (Arrays.equals(rawData, data)) {
				return false;
			}
			handleData(data);
			this.rawData = data;
		} catch (Exception e) {
			SimpleLoggerHolder.error("sumk.conf", e);
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception e2) {
				}
			}
		}
		return true;
	}

	protected abstract void handleData(byte[] data);

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

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

}
