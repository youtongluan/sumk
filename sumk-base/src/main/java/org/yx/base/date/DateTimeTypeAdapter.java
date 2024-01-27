/**
 * Copyright (C) 2016 - 2030 youtongluan.
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
package org.yx.base.date;

import java.io.IOException;
import java.util.Date;

import org.yx.util.StringUtil;
import org.yx.util.SumkDate;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public final class DateTimeTypeAdapter extends TypeAdapter<Date> {

	private String dateFormat = SumkDate.yyyy_MM_dd_HH_mm_ss_SSS;

	public void setDateFormat(String format) {
		if (StringUtil.isEmpty(format)) {
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

	private Date deserializeToDate(final String text) throws IOException {
		try {
			return SumkDate.of(text).toDate();
		} catch (Exception e) {
		}
		String num = text;
		if (num.contains(".")) {

			num = num.substring(0, num.indexOf('.'));
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
		out.value(SumkDate.format(value, this.dateFormat));
	}

}
