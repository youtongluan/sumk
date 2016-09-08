package org.yx.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class GsonUtil {
	
	private static Logger logger=LoggerFactory.getLogger(GsonUtil.class);

	private static Gson createGson(){
		return new GsonBuilder().disableHtmlEscaping()
		.registerTypeAdapter(Date.class, new DateTimeTypeAdapter())
		.create();
	}
	private static Gson[] gsons;
	private static volatile int index=0;
	static{
		gsons=new Gson[10];
		for(int i=0;i<gsons.length;i++){
			gsons[i]=createGson();
		}
	}

	private static Gson getGson() {
		int i=++index;
		if(i<0){
			index=Math.max(0, -index);
			i=0;
			logger.info("gson 下标重新生成 i:{},index:{}",i,index);
		}
		return gsons[i%gsons.length];

	}

	public static String toJson(Object obj) {
		return getGson().toJson(obj);
	}

	public static <T> T parseObject(String json, Class<T> clz) {
		return getGson().fromJson(json, clz);
	}
	
	public static <T> T fromJson(String json, Class<T> clz) {
		return getGson().fromJson(json, clz);
	}

	public static <T> T fromJson(String json, Type type) {
		return getGson().fromJson(json, type);
	}

	public static Object copyObject(Object source){
		if(source==null){
			return null;
		}
		String json=toJson(source);
		return fromJson(json, source.getClass());
	}
	
}


class DateTimeTypeAdapter extends TypeAdapter<Date> {
	private Logger logger=LoggerFactory.getLogger(DateTimeTypeAdapter.class);
	
	@Override
	public Date read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		return deserializeToDate(in.nextString());
	}

	private synchronized Date deserializeToDate(String json) {
		if (json == null) {
			return null;
		}
		try {
			
			try {
				return new Date(Long.parseLong(json));
			} catch (Exception e) {
			}
			double d = Double.parseDouble(json);
			return new Date((long) d);
		} catch (RuntimeException e) {
			logger.error("{}无法解析成日期类型",json);
			throw e;
		}
	}

	@Override
	public synchronized void write(JsonWriter out, Date value)
			throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}
		out.value(value.getTime());
	}

}
