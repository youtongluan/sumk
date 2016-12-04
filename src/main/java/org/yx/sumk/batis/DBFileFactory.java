package org.yx.sumk.batis;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.yx.conf.AppInfo;
import org.yx.util.Assert;

public class DBFileFactory implements DBResourceFactory {
	public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	@Override
	public DBResource create(String dbName) throws Exception {
		Assert.notNull(dbName);
		String uri = AppInfo.get("sumk.db.path." + dbName,
				CLASSPATH_URL_PREFIX + "db" + System.getProperty("file.separator") + dbName);
		uri = uri.trim();
		if (uri.startsWith(CLASSPATH_ALL_URL_PREFIX)) {
			return createWithAllClassPath(uri.substring(CLASSPATH_ALL_URL_PREFIX.length()));
		}
		if (uri.startsWith(CLASSPATH_URL_PREFIX)) {
			return createWithClassPath(uri.substring(CLASSPATH_URL_PREFIX.length()));
		}
		return new FileDBResource(new File(uri));
	}

	private DBResource createWithClassPath(String uri) throws URISyntaxException {
		URL url = DBFileFactory.class.getClassLoader().getResource(uri);
		return new FileDBResource(new File(url.toURI()));
	}

	private DBResource createWithAllClassPath(String uri) throws URISyntaxException {
		return createWithClassPath(uri);
	}

}
