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
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.yx.log.Log;
import org.yx.main.SumkThreadPool;

public class FileMonitor {

	public final static FileMonitor inst = new FileMonitor();
	private volatile boolean started;
	private List<FileHandler> handlers = new CopyOnWriteArrayList<>();
	private Map<String, Long> lastModif = new HashMap<String, Long>();

	private FileMonitor() {
	}

	public void addHandle(FileHandler h) {
		this.handlers.add(h);
	}

	public void start() {
		if (started) {
			return;
		}
		long seconds = Integer.getInteger("sumk.fileMonitor.period", 60);
		SumkThreadPool.scheduledExecutor.scheduleWithFixedDelay(() -> {
			for (FileHandler h : handlers) {
				handle(h, true);
			}
		}, seconds, seconds, TimeUnit.SECONDS);
		started = true;
	}

	public synchronized void handle(FileHandler h, boolean showLog) {
		URL[] urls = h.listFile();
		if (urls == null) {
			return;
		}
		for (URL url : urls) {
			File f = new File(url.getPath());
			String p = f.getAbsolutePath();
			if (!f.isFile() || !f.exists()) {
				if (!lastModif.containsKey(p)) {
					lastModif.put(p, System.currentTimeMillis());
					try (InputStream fin = url.openStream()) {
						h.deal(fin);
					} catch (Exception e) {
						Log.printStack(e);
					}
				}
				continue;
			}
			Long modify = lastModif.get(p);
			if (modify == null || f.lastModified() > modify) {
				lastModif.put(p, f.lastModified());
				if (showLog) {
					Log.get("sumk.SYS").info("##{} changed at {}", f, Instant.ofEpochMilli(lastModif.get(p)));
				}
				try (InputStream fin = url.openStream()) {
					h.deal(fin);
				} catch (Exception e) {
					Log.printStack(e);
				}
			}
		}
	}

}
