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
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.yx.common.StartOnceLifecycle;
import org.yx.log.RawLog;
import org.yx.main.SumkThreadPool;
import org.yx.util.CollectionUtil;

public class AppConfig extends StartOnceLifecycle implements SystemConfig {

	protected final String fileName;
	protected final int periodTime;
	protected Map<String, String> map = Collections.emptyMap();
	protected boolean showLog = true;
	protected ScheduledFuture<?> future;

	public AppConfig() {
		this(System.getProperty("appinfo", "app.properties"));
	}

	public AppConfig(String fileName) {
		this(fileName, 1000 * 60);
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
		RawLog.info("sumk.conf", "can not found " + this.fileName);
		return null;
	}

	private void handle() {
		try (InputStream in = openInputStream()) {
			if (in == null) {
				return;
			}
			Map<String, String> conf = CollectionUtil.loadMap(in, false);
			if (conf != null && !conf.equals(this.map)) {
				if (this.showLog) {
					RawLog.info("sumk.conf", fileName + " loaded");
				}
				onChange(conf);
				this.map = conf;
				AppInfo.notifyUpdate();
			}
		} catch (Exception e) {
			RawLog.error("sumk.conf", e.getMessage(), e);
		}
	}

	protected void onChange(Map<String, String> newConf) {

	}

	public boolean isShowLog() {
		return showLog;
	}

	public void setShowLog(boolean showLog) {
		this.showLog = showLog;
	}

	@Override
	protected void onStart() {
		this.handle();
		this.future = SumkThreadPool.scheduledExecutor().scheduleAtFixedRate(this::handle, this.periodTime,
				this.periodTime, TimeUnit.MILLISECONDS);
		RawLog.setLogger(RawLog.SLF4J_LOG);
	}

	@Override
	public Set<String> keys() {
		return new HashSet<>(this.map.keySet());
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

}
