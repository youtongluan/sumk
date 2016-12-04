package org.yx.sumk.batis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.Assert;

/**
 * 需要[]来分段<BR>
 * 以#注释
 * 
 * @author 游夏
 *
 */
public final class ResUtils {

	public static DBResource dbResource(String db) throws Exception {
		String resourceFactory = AppInfo.get("sumk.db.resource.factory." + db, "DBFileFactory");
		resourceFactory = "org.yx.sumk.batis." + resourceFactory;
		Class<?> factoryClz = ResUtils.class.getClassLoader().loadClass(resourceFactory);
		Assert.isTrue(DBResourceFactory.class.isAssignableFrom(factoryClz),
				resourceFactory + " should extend from DBResourceFactory");
		DBResourceFactory factory = (DBResourceFactory) factoryClz.newInstance();
		return factory.create(db);
	}

	public static Map<String, Map<String, String>> parseIni(String filename) throws FileNotFoundException {
		return parseIni(new FileInputStream(filename));
	}

	public static Map<String, Map<String, String>> parseIni(InputStream in) {
		return new IniFile(in).sections;
	}

	private static class IniFile {
		protected Map<String, Map<String, String>> sections = new HashMap<>();
		private String currentSecion;
		private Map<String, String> current;

		IniFile(InputStream in) {
			if (in == null) {
				SumkException.throwException(245323425, "ini stream cannot be null");
			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(in, AppInfo.systemCharset()));
				read(reader);
			} catch (Exception e) {
				Log.printStack(e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception e) {
					}
				}

			}
		}

		protected void read(BufferedReader reader) throws IOException {
			String line;
			while ((line = reader.readLine()) != null) {
				parseLine(line);
			}
		}

		protected void parseLine(String line) {
			line = line.trim();
			if (line.matches("^\\[.*\\]")) {
				currentSecion = line.replaceFirst("\\[(.*)\\]", "$1");
				current = new HashMap<>();
				sections.put(currentSecion, current);
			} else if (line.matches(".*=.*")) {
				if (current != null && !line.startsWith("#")) {
					int i = line.indexOf('=');
					String name = line.substring(0, i).trim();
					String value = line.substring(i + 1).trim();
					if (value.isEmpty()) {
						return;
					}
					current.put(name, value);
				}
			}
		}

		@Override
		public String toString() {
			return this.sections.toString();
		}
	}

}
