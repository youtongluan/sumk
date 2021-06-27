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
package org.yx.common.json;

import java.io.IOException;

import org.yx.annotation.doc.NotNull;
import org.yx.asm.ParamPojo;
import org.yx.asm.Parameters;
import org.yx.exception.SumkException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class ParamPojoTypeAdapter<T extends ParamPojo> extends TypeAdapter<T> {
	@NotNull
	private final Gson gson;

	@NotNull
	private final Parameters info;

	public ParamPojoTypeAdapter(Gson gson, Parameters info) {
		this.info = info;
		this.gson = gson;
	}

	@Override
	public T read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}

		String name = null;
		try {
			T pojo = info.createEmptyParamObj();
			Object[] objs = new Object[info.paramLength()];
			in.beginObject();
			while (in.hasNext()) {
				name = in.nextName();
				int index = info.getIndex(name);
				if (index < 0) {
					in.skipValue();
					continue;
				}
				objs[index] = gson.getAdapter(TypeToken.get(info.getParamType(index))).read(in);
			}
			in.endObject();
			pojo.setParams(objs);
			return pojo;
		} catch (Exception e) {
			throw new SumkException(-34534234, info.paramClz().getSimpleName() + "解析" + name + "字段出错：" + e.getMessage(),
					e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void write(JsonWriter out, ParamPojo pojo) throws IOException {
		if (pojo == null) {
			out.nullValue();
			return;
		}

		out.beginObject();
		Object[] objs = pojo.params();
		int len = info.paramLength();
		for (int i = 0; i < len; i++) {
			out.name(info.getParamName(i));
			TypeAdapter adapter = gson.getAdapter(TypeToken.get(info.getParamType(i)));
			adapter.write(out, objs[i]);
		}

		out.endObject();
	}
}