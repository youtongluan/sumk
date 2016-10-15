package org.yx.conf;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.yx.log.Log;

public class FileWatcher {

	public final static FileWatcher inst = new FileWatcher();
	private List<FileHandler> handlers = new CopyOnWriteArrayList<>();
	private Map<String, Long> lastModif = new HashMap<String, Long>();

	private FileWatcher() {
	}

	static {
		inst.start();
	}

	public void addHandle(FileHandler h) {
		this.handlers.add(h);
		handle(h, false);
	}

	public void start() {
		Thread t = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000);
					for (FileHandler h : handlers) {
						handle(h, true);
					}
				} catch (Exception e) {
					Log.printStack(e);
				}

			}

		}, "file-watcher");
		t.setDaemon(true);
		t.start();

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
					Log.get("SYS.11").info("##{} changed at {}", f, lastModif.get(p));
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
