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
package org.yx.asm;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.yx.bean.Loader;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.util.CollectionUtil;

public class Parameters {
	private final Type[] paramTypes;
	private final String[] paramNames;
	private final Class<? extends ParamPojo> paramClz;
	private final ParamPojo sample;

	public Parameters(Class<? extends ParamPojo> clz, String[] names, Type[] fieldsType) {
		this.paramTypes = Objects.requireNonNull(fieldsType);
		this.paramNames = Objects.requireNonNull(names);
		this.paramClz = Objects.requireNonNull(clz);
		try {
			this.sample = Loader.newInstance(this.paramClz);
		} catch (Exception e) {
			Logs.asm().error(clz.getName() + "初始化失败", e);
			throw new SumkException(-345354334, clz.getSimpleName() + "初始化失败");
		}
	}

	public List<Type> paramTypes() {
		return CollectionUtil.unmodifyList(paramTypes);
	}

	public List<String> paramNames() {
		return CollectionUtil.unmodifyList(paramNames);
	}

	public Class<? extends ParamPojo> paramClz() {
		return paramClz;
	}

	public int getIndex(String name) {
		for (int i = 0; i < paramNames.length; i++) {
			if (paramNames[i].equals(name)) {
				return i;
			}
		}
		return -1;
	}

	public Type getParamType(int index) {
		return this.paramTypes[index];
	}

	public String getParamName(int index) {
		return this.paramNames[index];
	}

	public ParamPojo createParamPojo(Map<String, Object> map) throws Exception {
		Object[] objs = new Object[paramNames.length];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = map.get(paramNames[i]);
		}
		ParamPojo pojo = createEmptyParamObj();
		pojo.setParams(objs);
		return pojo;
	}

	@SuppressWarnings("unchecked")
	public <T extends ParamPojo> T createEmptyParamObj() {
		return (T) sample.createEmpty();
	}

	public int paramLength() {
		return this.paramNames.length;
	}
}
