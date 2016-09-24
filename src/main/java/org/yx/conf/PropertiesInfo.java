package org.yx.conf;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesInfo implements FileHandler {

	protected String fileName;

	public PropertiesInfo(String fileName) {
		super();
		this.fileName = fileName;
		FileWatcher.inst.addHandle(this);
	}

	Properties pro = new Properties();

	public String get(String key) {
		return pro.getProperty(key);
	}

	@Override
	public File[] listFile() {
		java.net.URL url = PropertiesInfo.class.getClassLoader().getResource(fileName);
		if (url == null) {
			return new File[0];
		}
		return new File[] { new File(url.getPath()) };
	}

	@Override
	public void deal(InputStream in) throws Exception {
		Properties temp = new Properties();
		temp.load(in);
		pro = temp;
	}

}
