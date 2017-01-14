/**
 * Copyright (C) 2016 - 2017 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
