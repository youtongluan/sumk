package org.yx.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 从本地文件读取数据库的资源配置
 * 
 * @author 游夏
 *
 */
public class LocalDBResourceFactory implements SingleResourceFactory {

	private InputStream fileInClassPath(String uri, String dbName) throws URISyntaxException, FileNotFoundException {
		URL url = this.getClass().getClassLoader().getResource(uri);
		File parent = new File(url.toURI());
		return new FileInputStream(new File(parent, "db.ini"));
	}

	public InputStream openInput(String dbName) throws Exception {
		String uri = AppInfo.get("sumk.db.config.path." + dbName,
				AppInfo.CLASSPATH_URL_PREFIX + "db" + System.getProperty("file.separator") + dbName);
		uri = uri.trim();
		if (uri.startsWith(AppInfo.CLASSPATH_ALL_URL_PREFIX)) {
			return fileInClassPath(uri.substring(AppInfo.CLASSPATH_ALL_URL_PREFIX.length()), dbName);
		}
		if (uri.startsWith(AppInfo.CLASSPATH_URL_PREFIX)) {
			return fileInClassPath(uri.substring(AppInfo.CLASSPATH_URL_PREFIX.length()), dbName);
		}
		return new FileInputStream(new File(uri, "db.ini"));
	}

}
