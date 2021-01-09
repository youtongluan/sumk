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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.yx.log.RawLog;
import org.yx.util.CollectionUtil;
import org.yx.util.IOUtil;
import org.yx.util.Task;

public class AppConfig extends AbstractRefreshableSystemConfig {

	protected final String fileName;
	protected final int periodTime;
	protected Map<String, String> map = Collections.emptyMap();
	protected boolean showLog = true;
	private boolean isFirst = true;
	protected ScheduledFuture<?> future;

	public AppConfig() {
		this(System.getProperty("sumk.appinfo", "app.properties"));
	}

	public AppConfig(String fileName) {
		this(fileName, Integer.getInteger("sumk.appinfo.period", 1000 * 30));
	}

	public AppConfig(String fileName, int periodTimeMS) {
		this.fileName = Objects.requireNonNull(fileName);
		this.periodTime = Math.max(periodTimeMS, 1000);
	}

	private InputStream openInputStream() throws FileNotFoundException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
		if (in != null) {
			return in;
		}
		File f = new File(fileName);
		if (f.exists()) {
			return new FileInputStream(f);
		}
		RawLog.info(LOG_NAME, "can not found " + this.fileName);
		return null;
	}

	private void handle() {
		try (InputStream in = openInputStream()) {
			if (in == null) {
				return;
			}
			byte[] bs = IOUtil.readAllBytes(in, true);
			Map<String, String> conf = CollectionUtil.fillConfigFromText(new HashMap<>(),
					new String(bs, StandardCharsets.UTF_8));
			if (conf != null && !conf.equals(this.map)) {
				if (this.showLog) {
					RawLog.info(LOG_NAME, fileName + " loaded");
				}
				this.map = conf;
				if (!isFirst) {
					this.onRefresh();
				}
			}
			this.isFirst = false;
		} catch (Exception e) {
			RawLog.error(LOG_NAME, e.getMessage(), e);
		}
	}

	public boolean isShowLog() {
		return showLog;
	}

	public void setShowLog(boolean showLog) {
		this.showLog = showLog;
	}

	@Override
	protected void init() {
		this.handle();
		this.future = Task.scheduleAtFixedRate(this::handle, this.periodTime, this.periodTime, TimeUnit.MILLISECONDS);
	}

	@Override
	public Set<String> keys() {
		return Collections.unmodifiableSet(this.map.keySet());
	}

	@Override
	public synchronized void stop() {
		if (this.future != null) {
			this.future.cancel(false);
		}
		this.started = false;
	}

	@Override
	public String get(String key) {
		return map.get(key);
	}

	@Override
	public String toString() {
		return String.valueOf(map);
	}

	@Override
	public Map<String, String> values() {
		return Collections.unmodifiableMap(map);
	}

}
