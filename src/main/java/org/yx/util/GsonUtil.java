package org.yx.util;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Date;

import org.yx.common.DateTimeTypeAdapter;
import org.yx.db.dao.Pojo;
import org.yx.log.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

	/**
	 * 不能用于集合、数组
	 * 
	 * @param source
	 * @return
	 */
	public static Object copyObject(Object source) {
		if (source == null) {
			return null;
		}
		if (Pojo.class.isInstance(source)) {
			try {
				return ((Pojo) source).clone();
			} catch (CloneNotSupportedException e) {
				if (Log.isTraceEnable("gson")) {
					Log.printStack(e);
				}
			}
		}
		String json = toJson(source);
		return fromJson(json, source.getClass());
	}

}
