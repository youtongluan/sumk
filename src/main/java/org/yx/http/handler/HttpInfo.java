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
package org.yx.http.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.yx.http.Upload;
import org.yx.http.Web;

public final class HttpInfo {
	public static interface Visitor {
		Object visit(HttpInfo info) throws Throwable;
	}

	final Method m;
	final String[] argNames;

	final Class<?>[] argTypes;
	final Field[] fields;
	final Web action;
	final Upload upload;
	final Object obj;

	final Class<?> argClz;

	Field[] getFields() {
		return fields;
	}

	/**
	 * 被代理过的方法
	 * 
	 * @return
	 */
	public Method getM() {
		return m;
	}

	public String[] getArgNames() {
		return argNames;
	}

	public Class<?>[] getArgTypes() {
		return argTypes;
	}

	public Web getAction() {
		return action;
	}

	/**
	 * 被代理的对象
	 * 
	 * @return
	 */
	public Object getObj() {
		return obj;
	}

	Class<?> getArgClz() {
		return argClz;
	}

	public Upload getUpload() {
		return upload;
	}

	public HttpInfo(Object obj, Method m, Class<?> argClz, String[] argNames, Class<?>[] argTypes, Web action,
			Upload upload) {
		super();
		this.obj = obj;
		this.m = m;
		this.argClz = argClz;
		this.argNames = argNames;
		this.argTypes = argTypes;
		this.action = action;
		this.m.setAccessible(true);
		this.upload = upload;
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
