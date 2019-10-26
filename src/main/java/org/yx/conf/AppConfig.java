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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.yx.util.CollectionUtil;

public class AppConfig implements SystemConfig {

	protected boolean started;
	protected final String fileName;
	protected Map<String, String> map = new HashMap<>();
	protected int periodTime = 1000 * 1;
	protected boolean showLog = true;

	public AppConfig() {
		this(System.getProperty("appinfo", "app.properties"));
	}

	public AppConfig(String fileName) {
		this.fileName = fileName;
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
		return null;
	}

	private void handle() {
		try (InputStream in = openInputStream()) {
			Map<String, String> conf = CollectionUtil.loadMap(in, false);
			if (conf != null && !conf.equals(map)) {
				if (this.showLog) {
					System.out.println("app conf changed at " + new Date());
				}
				map = conf;
				AppInfo.notifyUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPeriodTime() {
		return periodTime;
	}

	public void setPeriodTime(int periodTimeMS) {
		if (periodTimeMS >= 1000) {
			this.periodTime = periodTimeMS;
		}
	}

	public boolean isShowLog() {
		return showLog;
	}

	public void setShowLog(boolean showLog) {
		this.showLog = showLog;
	}

	@Override
	public synchronized void start() {
		if (started) {
			return;
		}
		started = true;
		this.handle();
		Thread t = new Thread(() -> {
			for (;;) {
				try {
					Thread.sleep(this.periodTime);
					if (!this.started) {
						return;
					}
					handle();
				} catch (Exception e) {
					if (InterruptedException.class.isInstance(e)) {
						System.out.println("exit because of InterruptedException");
						return;
					}
					e.printStackTrace();
				}
			}
		}, "appinfo-thread");
		t.setDaemon(true);
		t.start();
	}

	@Override
	public Set<String> keys() {
		return new HashSet<>(this.map.keySet());
	}

	@Override
	public synchronized void stop() {
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
