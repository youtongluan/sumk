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

import org.yx.bean.aop.asm.ParamPojo;
import org.yx.bean.aop.asm.ParamPojos;
import org.yx.bean.aop.asm.MethodPojo;
import org.yx.log.Logs;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class ParamPojoTypeAdapterFactory implements TypeAdapterFactory {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if (!ParamPojo.class.isAssignableFrom(type.getRawType())) {
			return null;
		}
		Class<T> clz = (Class<T>) type.getRawType();
		MethodPojo en = ParamPojos.get((Class<? extends ParamPojo>) clz);
		if (en == null) {
			Logs.system().info("{} cannot found ParamPojoInfo", clz.getSimpleName());
			return null;
		}
		if (Logs.system().isDebugEnabled()) {
			Logs.system().debug("add ParamPojoTypeAdapter for {}", clz.getSimpleName());
		}
		return new ParamPojoTypeAdapter(gson, en);
	}

}
