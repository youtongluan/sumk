package org.yx.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DateTimeTypeAdapter extends TypeAdapter<Date> {

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
