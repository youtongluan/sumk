package org.yx.util;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class GsonUtil {

	private static Gson createGson() {
		return new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(Date.class, new DateTimeTypeAdapter())
				.create();
	}

	private static Gson[] gsons;
	private static volatile int index = 0;
	static {
		gsons = new Gson[8];
		for (int i = 0; i < gsons.length; i++) {
			gsons[i] = createGson();
		}
	}

	private static Gson getGson() {
		int i = ++index;
		return gsons[i & 0x7];

	}

	public static String toJson(Object obj) {
		return getGson().toJson(obj);
	}

	public static String toJson(Object obj, Type type) {
		return getGson().toJson(obj, type);
	}

	public static void toJson(Object obj, Type type, Appendable writer) {
		getGson().toJson(obj, type, writer);
	}

	public static <T> T fromJson(String json, Class<T> clz) {
		return getGson().fromJson(json, clz);
	}

	public static <T> T fromJson(String json, Type type) {
		return getGson().fromJson(json, type);
	}

	public static <T> T fromJson(Reader reader, Class<T> clz) {
		return getGson().fromJson(reader, clz);
	}

	public static Object copyObject(Object source) {
		if (source == null) {
			return null;
		}
		String json = toJson(source);
		return fromJson(json, source.getClass());
	}

}

class DateTimeTypeAdapter extends TypeAdapter<Date> {

	@Override
	public Date read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		return deserializeToDate(in.nextString());
	}

	private Date deserializeToDate(final String json) throws IOException {
		if (json == null) {
			return null;
		}

		if (json.contains(":")) {
			SimpleDateFormat format = null;
			if (json.length() == 19) {
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			} else if (json.length() == 23) {
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			}
			if (format != null) {
				try {
					return format.parse(json);
				} catch (Exception e) {
				}
			}
			throw new IOException(json + " cannot convert to Date");
		}

		String num = json;
		if (num.contains(".")) {

			num = num.substring(0, num.indexOf("."));
		}
		return new Date(Long.parseLong(num));
	}

	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}
		out.value(value.getTime());
	}

}
