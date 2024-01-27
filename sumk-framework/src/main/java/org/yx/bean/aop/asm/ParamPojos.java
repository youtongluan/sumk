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
package org.yx.bean.aop.asm;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParamPojos {
	private static Map<Class<? extends ParamPojo>, MethodPojo> map = new ConcurrentHashMap<>();

	public static MethodPojo get(Class<? extends ParamPojo> clz) {
		return map.get(clz);
	}

	public synchronized static MethodPojo create(MethodParamInfo p) throws Exception {
		Class<? extends ParamPojo> clz = new ParamPojoClassFactory(p).create();
		MethodPojo info = map.get(clz);
		if (info != null) {
			return info;
		}
		Type[] fs = new Type[p.getArgNames().length];
		for (int i = 0; i < fs.length; i++) {
			fs[i] = clz.getDeclaredField(p.getArgNames()[i]).getGenericType();
		}
		info = new MethodPojo(clz, p.getArgNames(), fs);
		map.put(info.paramClz(), info);
		return info;
	}
}
