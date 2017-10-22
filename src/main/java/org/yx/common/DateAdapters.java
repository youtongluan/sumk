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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.yx.conf.AppInfo;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DateAdapters {

	public static void register(GsonBuilder builder, String module) {
		if (AppInfo.getBoolean(module + ".date.adaper.disable", false)) {
			return;
		}
		builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
		builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
		builder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
	}

	public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

		@Override
		public void write(JsonWriter out, LocalDateTime value) throws IOException {
			out.value(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		}

		@Override
		public LocalDateTime read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String v = in.nextString();
			v = v.replace(' ', 'T');
			return LocalDateTime.parse(v, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}

	}

	public static class LocalDateAdapter extends TypeAdapter<LocalDate> {

		private DateTimeFormatter formater = DateTimeFormatter.ISO_LOCAL_DATE;

		@Override
		public void write(JsonWriter out, LocalDate value) throws IOException {
			out.value(value.format(formater));
		}

		@Override
		public LocalDate read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String v = in.nextString();
			return LocalDate.parse(v, formater);
		}

	}

	public static class LocalTimeAdapter extends TypeAdapter<LocalTime> {

		private DateTimeFormatter formater = DateTimeFormatter.ISO_LOCAL_TIME;

		@Override
		public void write(JsonWriter out, LocalTime value) throws IOException {
			out.value(value.format(formater));
		}

		@Override
		public LocalTime read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String v = in.nextString();
			return LocalTime.parse(v, formater);
		}

	}
}
