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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.yx.util.SumkDate;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public final class DateAdapters {

	public static void registerAll(GsonBuilder builder) {
		builder.registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter);
		builder.registerTypeAdapter(LocalDate.class, localDateAdapter);
		builder.registerTypeAdapter(LocalTime.class, localTimeAdapter);
		builder.registerTypeAdapter(SumkDate.class, sumkDateAdapter);
		builder.registerTypeAdapter(java.sql.Date.class, sqlDateAdapter);
	}

	public static final TypeAdapter<LocalDateTime> localDateTimeAdapter = new TypeAdapter<LocalDateTime>() {

		@Override
		public void write(JsonWriter out, LocalDateTime value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}
			out.value(SumkDate.of(value).to_yyyy_MM_dd_HH_mm_ss_SSS());
		}

		@Override
		public LocalDateTime read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String v = in.nextString();
			return SumkDate.of(v).toLocalDateTime();
		}
	};

	public static final TypeAdapter<LocalDate> localDateAdapter = new TypeAdapter<LocalDate>() {

		@Override
		public void write(JsonWriter out, LocalDate value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}
			out.value(SumkDate.of(value, null).to_yyyy_MM_dd());
		}

		@Override
		public LocalDate read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String v = in.nextString();
			return SumkDate.of(v, SumkDate.yyyy_MM_dd).toLocalDate();
		}
	};

	public static final TypeAdapter<LocalTime> localTimeAdapter = new TypeAdapter<LocalTime>() {

		@Override
		public void write(JsonWriter out, LocalTime value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}
			out.value(SumkDate.of(null, value).to_HH_mm_ss_SSS());
		}

		@Override
		public LocalTime read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String v = in.nextString();
			return SumkDate.of(v, SumkDate.HH_mm_ss_SSS).toLocalTime();
		}
	};

	public static final TypeAdapter<SumkDate> sumkDateAdapter = new TypeAdapter<SumkDate>() {

		@Override
		public void write(JsonWriter out, SumkDate sd) throws IOException {
			if (sd == null) {
				out.nullValue();
				return;
			}
			out.value(sd.to_yyyy_MM_dd_HH_mm_ss_SSS());
		}

		@Override
		public SumkDate read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String v = in.nextString();
			try {
				return SumkDate.of(v);
			} catch (Exception e) {
				String num = v;
				if (num.contains(".")) {
					num = num.substring(0, num.indexOf('.'));
				}
				return SumkDate.of(Long.parseLong(num));
			}
		}
	};

	public static final TypeAdapter<java.sql.Date> sqlDateAdapter = new TypeAdapter<java.sql.Date>() {

		@Override
		public void write(JsonWriter out, java.sql.Date d) throws IOException {
			if (d == null) {
				out.nullValue();
				return;
			}
			out.value(SumkDate.of(d).to_yyyy_MM_dd());
		}

		@Override
		public java.sql.Date read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String v = in.nextString();
			return TimeUtil.toType(SumkDate.of(v, SumkDate.yyyy_MM_dd), java.sql.Date.class, true);
		}
	};
}
