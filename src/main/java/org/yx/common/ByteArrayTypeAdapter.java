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
package org.yx.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.yx.util.secury.Base64Util;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class ByteArrayTypeAdapter extends TypeAdapter<byte[]> {

	private ByteArrayTypeAdapter() {

	}

	public static final ByteArrayTypeAdapter inst = new ByteArrayTypeAdapter();

	@Override
	public void write(JsonWriter out, byte[] value) throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}
		out.value(new String(Base64.getEncoder().encode(value), StandardCharsets.UTF_8));

	}

	@Override
	public byte[] read(JsonReader in) throws IOException {
		JsonToken token = in.peek();
		if (token == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		if (token == JsonToken.STRING) {
			String s = in.nextString();
			if (s.isEmpty()) {
				return new byte[0];
			}
			return Base64Util.decode(s.getBytes(StandardCharsets.UTF_8));
		}
		if (token == JsonToken.BEGIN_ARRAY) {
			return rawRead(in);
		}
		throw new IOException(token + " is not valid byte array token");
	}

	private byte[] rawRead(JsonReader in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(128);
		in.beginArray();
		try {
			while (in.hasNext()) {
				if (in.peek() == JsonToken.NULL) {
					in.nextNull();
					continue;
				}
				out.write(in.nextInt());
			}
		} catch (NumberFormatException e) {
			throw new JsonSyntaxException(e);
		}
		in.endArray();
		out.flush();
		return out.toByteArray();
	}

}
