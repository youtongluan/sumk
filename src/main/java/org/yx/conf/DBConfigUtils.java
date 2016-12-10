package org.yx.conf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.yx.bean.Loader;
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
public final class DBConfigUtils {

	public static InputStream openConfig(String db) throws Exception {
		String resourceFactory = AppInfo.get("sumk.db.conf.factory." + db, LocalDBResourceFactory.class.getName());
		Class<?> factoryClz = Loader.loadClass(resourceFactory);
		Assert.isTrue(SingleResourceFactory.class.isAssignableFrom(factoryClz),
				resourceFactory + " should extend from SingleResourceFactory");
		SingleResourceFactory factory = (SingleResourceFactory) factoryClz.newInstance();
		return factory.openInput(db);
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
