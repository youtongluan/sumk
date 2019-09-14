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
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

import org.yx.log.Log;
import org.yx.main.SumkThreadPool;
import org.yx.util.helper.ArrayHelper;

public class FileMonitor {

	public final static FileMonitor inst = new FileMonitor();
	private volatile boolean started;
	private FileHandler[] handlers;
	private Map<String, Long> lastModif = new HashMap<String, Long>();

	private FileMonitor() {
	}

	public synchronized void addHandle(FileHandler h) {
		this.handlers = new ArrayHelper().add(handlers, h, new FileHandlerArrayFactory());
	}

	public synchronized void remove(FileHandler h) {
		this.handlers = new ArrayHelper().remove(handlers, h, new FileHandlerArrayFactory());
	}

	public synchronized void start() {
		if (started) {
			return;
		}
		long seconds = Integer.getInteger("sumk.fileMonitor.period", 60);
		SumkThreadPool.scheduledExecutor.scheduleWithFixedDelay(() -> {
			FileHandler[] hs = handlers;
			if (hs == null || hs.length == 0) {
				return;
			}
			for (FileHandler h : hs) {
				try {
					handle(h, true);
				} catch (Exception e) {
					Log.printStack("sumk.error", e);
				}
			}
		}, seconds, seconds, TimeUnit.SECONDS);
		started = true;
	}

	public synchronized void handle(FileHandler h, boolean showLog) throws URISyntaxException {
		URL[] urls = h.listFile();
		if (urls == null) {
			return;
		}
		for (URL url : urls) {
			final String p = url.getPath();
			if (!lastModif.containsKey(p)) {
				lastModif.put(p, System.currentTimeMillis());
				try (InputStream fin = url.openStream()) {
					h.deal(fin);
				} catch (Exception e) {
					Log.printStack("sumk.error", e);
				}
				continue;
			}
			if (!"file".equals(url.getProtocol())) {
				continue;
			}
			File f = new File(url.toURI());
			Long modify = lastModif.get(p);
			if (f.lastModified() > modify) {
				lastModif.put(p, f.lastModified());
				if (showLog) {
					Log.get("sumk.SYS").info("##{} changed at {}", f.getName(), Instant.ofEpochMilli(lastModif.get(p)));
				}
				try (InputStream fin = url.openStream()) {
					h.deal(fin);
				} catch (Exception e) {
					Log.printStack("sumk.error", e);
				}
			}
		}
	}

	private static class FileHandlerArrayFactory implements IntFunction<FileHandler[]> {

		@Override
		public FileHandler[] apply(int length) {
			return new FileHandler[length];
		}

	}

}
