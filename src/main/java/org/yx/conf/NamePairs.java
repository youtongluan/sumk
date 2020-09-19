package org.yx.conf;

import static org.yx.conf.AppInfo.LN;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public final class NamePairs {

	private final Map<String, String> map;

	private final String data;

	public NamePairs(final String data) {
		this.data = data;
		this.map = StringUtil.isEmpty(data) ? Collections.emptyMap()
				: CollectionUtil.fillMapFromText(new LinkedHashMap<String, String>(),
						data.trim().replace("\r\n", LN).replace("\r", LN), LN, "=");
	}

	public NamePairs(final Map<String, String> map) {
		this.map = map;
		this.data = map == null ? "" : CollectionUtil.saveMapToText(map, LN, "=");
	}

	private NamePairs(Map<String, String> map, String data) {
		this.map = map;
		this.data = data;
	}

	public String getRawData() {
		return data;
	}

	public Map<String, String> values() {
		return new HashMap<>(this.map);
	}

	public NamePairs unmodify() {
		return new NamePairs(Collections.unmodifiableMap(this.map), this.data);
	}

	public String getValue(String key) {
		if (key == null) {
			return null;
		}
		return map.get(key);
	}

	@Override
	public String toString() {
		return this.data;
	}

	public Set<String> keys() {
		return new HashSet<>(this.map.keySet());
	}

	public InputStream toInputStream(Charset charset) {
		if (this.data == null || this.data.isEmpty()) {
			return new ByteArrayInputStream(new byte[0]);
		}
		return new ByteArrayInputStream(this.data.getBytes(charset));
	}

	public byte[] toBytes(Charset charset) {
		if (this.data == null || this.data.isEmpty()) {
			return new byte[0];
		}
		return this.data.getBytes(charset);
	}

	public static NamePairs createByString(String data) {
		return new NamePairs(data);
	}

	public static NamePairs createByMap(Map<String, String> map) {
		return new NamePairs(map);
	}
}