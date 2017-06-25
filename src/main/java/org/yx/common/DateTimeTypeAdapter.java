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
import java.util.Date;

import org.yx.util.DateUtils;
import org.yx.util.StringUtils;
import org.yx.util.date.SumkDate;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DateTimeTypeAdapter extends TypeAdapter<Date> {

	private String dateFormat;

	public void setDateFormat(String format) {
		if (StringUtils.isEmpty(format)) {
			this.dateFormat = null;
		}
		this.dateFormat = format;
	}

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
			String format = null;
			if (this.dateFormat != null && json.length() == this.dateFormat.length()) {
				format = this.dateFormat;
			} else if (json.length() == 19) {
				format = SumkDate.DATE_TIME;
			} else if (json.length() == 23) {
				format = SumkDate.DATE_TIME_MILS;
			}
			if (format != null) {
				try {
					return DateUtils.parse(json, format);
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
		if (this.dateFormat == null) {
			out.value(value.getTime());
			return;
		}
		out.value(DateUtils.toString(value, this.dateFormat));
	}

}
