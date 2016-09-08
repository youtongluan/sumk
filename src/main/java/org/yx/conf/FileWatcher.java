package org.yx.conf;

import java.io.File;
import java.io.FileInputStream;
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
		handle(h,false);
	}

	public void start() {
		Thread t=new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000);
					for (FileHandler h : handlers) {
						handle(h,true);
					}
				} catch (Exception e) {
					Log.get("SYS.10").error(e);
				}

			}

		} , "file-watcher");
		t.setDaemon(true);
		t.start();

	}

	private synchronized void handle(FileHandler h,boolean showLog) {
		File[] fs = h.listFile();
		for (File f : fs) {
			String p = f.getAbsolutePath();
			Long modify = lastModif.get(p);
			if (modify == null || f.lastModified() > modify) {
				lastModif.put(p, f.lastModified());
				if(showLog){
					Log.get("SYS.11").info("##{} changed at {}",f,lastModif.get(p));
				}
				try (FileInputStream fin = new FileInputStream(f)) {
					h.deal(fin);
				} catch (Exception e) {
					Log.get("SYS.12").error(e);
				}
			}
		}
	}

}
