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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.yx.asm.ArgPojo;
import org.yx.validate.Param;
import org.yx.validate.ParamInfo;

public abstract class CalleeNode {
	public static interface Visitor {
		Object visit(CalleeNode info) throws Throwable;
	}

	public final Method method;

	public final String[] argNames;

	public final Class<?>[] argTypes;

	public final ParamInfo[] paramInfos;

	public final Object obj;

	public final Class<? extends ArgPojo> argClz;

	public final Field[] fields;

	public CalleeNode(Object obj, Method proxyMethod, Class<? extends ArgPojo> argClz, String[] argNames,
			Class<?>[] argTypes, Param[] params) {
		this.obj = obj;
		this.method = proxyMethod;
		this.argClz = argClz;
		this.argNames = argNames;
		this.argTypes = argTypes;
		this.method.setAccessible(true);
		this.paramInfos = params == null || params.length == 0 ? null : new ParamInfo[params.length];
		if (this.paramInfos != null) {
			for (int i = 0; i < this.paramInfos.length; i++) {
				Param p = params[i];
				if (p == null) {
					continue;
				}
				paramInfos[i] = new ParamInfo(p, argNames[i], argTypes[i]);
			}
		}
		if (argClz != null) {
			this.fields = argClz.getFields();
			Arrays.stream(fields).forEachOrdered(f -> f.setAccessible(true));
		} else {
			this.fields = null;
		}
	}

	public Object accept(Visitor visitor) throws Throwable {
		return visitor.visit(this);
	}

}
