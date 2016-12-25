package org.yx.sumk.batis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.conf.AppInfo;
import org.yx.conf.MultiResourceFactory;
import org.yx.log.Log;

public class LocalSqlXmlFactory implements MultiResourceFactory {

	private File fileInClassPath(String uri) throws URISyntaxException, FileNotFoundException {
		URL url = this.getClass().getClassLoader().getResource(uri);
		return new File(url.toURI());
	}

	public File getParent(String dbName) throws Exception {
		String uri = AppInfo.get("sumk.db.batis.path." + dbName,
				AppInfo.CLASSPATH_URL_PREFIX + "batis" + System.getProperty("file.separator") + dbName);
		uri = uri.trim();
		if (uri.startsWith(AppInfo.CLASSPATH_ALL_URL_PREFIX)) {
			return fileInClassPath(uri.substring(AppInfo.CLASSPATH_ALL_URL_PREFIX.length()));
		}
		if (uri.startsWith(AppInfo.CLASSPATH_URL_PREFIX)) {
			return fileInClassPath(uri.substring(AppInfo.CLASSPATH_URL_PREFIX.length()));
		}
		return new File(uri);
	}

	@Override
	public Map<String, InputStream> openInputs(String db) throws Exception {
		Map<String, InputStream> map = new HashMap<>();
		List<File> xmlFiles = new ArrayList<>();
		parseFileList(xmlFiles, getParent(db));
		for (File f : xmlFiles) {
			Log.get("sumk.db.batis").debug("mybatis file:{}", f.getAbsolutePath());
			map.put(f.getAbsolutePath(), new FileInputStream(f));
		}
		return map;
	}

	public void parseFileList(List<File> filelist, File parent) {
		File[] files = parent.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					parseFileList(filelist, f);
				} else if (f.getName().endsWith(".xml")) {
					filelist.add(f);
				}
			}

		}
	}

}
