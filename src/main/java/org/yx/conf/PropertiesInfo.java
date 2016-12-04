package org.yx.conf;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.yx.log.Log;
import org.yx.util.CollectionUtils;

public class PropertiesInfo implements FileHandler {

	protected String fileName;

	public PropertiesInfo(String fileName) {
		super();
		this.fileName = fileName;
		FileMonitor.inst.addHandle(this);
	}

	Map<String, String> pro = new HashMap<>();

	public String get(String key) {
		return pro.get(key);
	}

	@Override
	public URL[] listFile() {
		URL url = PropertiesInfo.class.getClassLoader().getResource(fileName);
		if (url == null) {
			Log.get(PropertiesInfo.class).info("{} cannot found", fileName);
			return null;
		}
		return new URL[] { url };
	}

	@Override
	public void deal(InputStream in) throws Exception {
		if (in == null) {
			return;
		}
		pro = Collections.unmodifiableMap(CollectionUtils.loadMap(in));
	}

}
