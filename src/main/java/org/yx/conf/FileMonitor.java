/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.yx.log.Log;
import org.yx.main.SumkServer;

public class FileMonitor {

	public final static FileMonitor inst = new FileMonitor();
	private List<FileHandler> handlers = new CopyOnWriteArrayList<>();
	private Map<String, Long> lastModif = new HashMap<String, Long>();

	private FileMonitor() {
	}

	static {
		inst.start();
	}

	public void addHandle(FileHandler h) {
		this.handlers.add(h);
		handle(h, false);
	}

	public void start() {
		SumkServer.runDeamon(() -> {
			Thread.sleep(1000);
			for (FileHandler h : handlers) {
				handle(h, true);
			}
		}, "file-watcher");

	}

	private synchronized void handle(FileHandler h, boolean showLog) {
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
					Log.get("sumk.SYS").info("##{} changed at {}", f, lastModif.get(p));
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
