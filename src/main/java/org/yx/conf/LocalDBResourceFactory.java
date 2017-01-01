package org.yx.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * 从本地文件读取数据库的资源配置
 * 
 * @author 游夏
 *
 */
public class LocalDBResourceFactory implements SingleResourceFactory {

	private InputStream fileInClassPath(String uri) throws URISyntaxException, FileNotFoundException {
		return this.getClass().getClassLoader().getResourceAsStream(uri);
	}

	public InputStream openInput(String dbName) throws Exception {
		String uri = AppInfo.get("sumk.db.config.path", AppInfo.CLASSPATH_URL_PREFIX + "db/#.ini");
		uri = uri.trim().replace("#", dbName);
		if (uri.startsWith(AppInfo.CLASSPATH_ALL_URL_PREFIX)) {
			return fileInClassPath(uri.substring(AppInfo.CLASSPATH_ALL_URL_PREFIX.length()));
		}
		if (uri.startsWith(AppInfo.CLASSPATH_URL_PREFIX)) {
			return fileInClassPath(uri.substring(AppInfo.CLASSPATH_URL_PREFIX.length()));
		}
		return new FileInputStream(new File(uri));
	}

}
