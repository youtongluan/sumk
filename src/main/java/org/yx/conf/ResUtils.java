package org.yx.conf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yx.exception.SystemException;
import org.yx.log.Log;

/**
 * 需要[]来分段<BR>
 * 以#注释
 * 
 * @author youtl
 *
 */
public final class ResUtils {

	public final static String CLASSPATH_ALL = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;
	public final static String CLASSPATH = PathMatchingResourcePatternResolver.CLASSPATH_URL_PREFIX;

	public static HashMap<String, Properties> parseIni(String filename) throws FileNotFoundException {
		return parseIni(new FileInputStream(filename));
	}

	public static HashMap<String, Properties> parseIni(InputStream in) {
		return new IniFile(in).sections;
	}

	public static InputStream getResAsInputStream(String path) throws IOException {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource r = resolver.getResource(path);
		return r == null ? null : r.getInputStream();
	}

	private static class IniFile {
		protected HashMap<String, Properties> sections = new HashMap<>();
		private String currentSecion;
		private Properties current;

		IniFile(InputStream in) {
			if (in == null) {
				SystemException.throwException(245323425, "ini stream cannot be null");
			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
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
				current = new Properties();
				sections.put(currentSecion, current);
			} else if (line.matches(".*=.*")) {
				if (current != null && !line.startsWith("#")) {
					int i = line.indexOf('=');
					String name = line.substring(0, i).trim();
					String value = line.substring(i + 1).trim();
					if (value.isEmpty()) {
						return;
					}
					current.setProperty(name, value);
				}
			}
		}

		@Override
		public String toString() {
			return this.sections.toString();
		}
	}

}
