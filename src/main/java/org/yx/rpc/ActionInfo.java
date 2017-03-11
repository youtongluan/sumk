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
package org.yx.rpc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.yx.common.BizExcutor;
import org.yx.exception.SumkException;
import org.yx.rpc.server.intf.ActionContext;
import org.yx.util.GsonUtil;

/**
 * soa服务的信息
 * 
 * @author 游夏
 *
 */
public final class ActionInfo {

	private Method m;
	private String[] argNames;

	private Class<?>[] argTypes;
	private Field[] fields;
	private Soa action;
	private Object obj;

	private Class<?> argClz;

	public Field[] getFields() {
		return fields;
	}

	public Method getM() {
		return m;
	}

	public String[] getArgNames() {
		return argNames;
	}

	public Class<?>[] getArgTypes() {
		return argTypes;
	}

	public Soa getAction() {
		return action;
	}

	public Object getObj() {
		return obj;
	}

	public Class<?> getArgClz() {
		return argClz;
	}

	public ActionInfo(Object obj, Method m, Class<?> argClz, String[] argNames, Class<?>[] argTypes, Soa action) {
		super();
		this.obj = obj;
		this.m = m;
		this.argClz = argClz;
		this.argNames = argNames;
		this.argTypes = argTypes;
		this.action = action;
		this.m.setAccessible(true);
		if (argClz != null) {
			this.fields = argClz.getFields();
			Arrays.stream(fields).forEachOrdered(f -> f.setAccessible(true));
		}
	}

	/**
	 * 使用参数执行方法，然后方法结果
	 * 
	 * @param args
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Object invokeByJsonArg(String args) throws Throwable {
		if (argTypes == null || argTypes.length == 0) {
			return BizExcutor.exec(m, obj, null, null);
		}
		Object[] params = new Object[getArgTypes().length];
		if (getArgClz() == null) {

			return null;
		}
		Object argObj = GsonUtil.fromJson(args, argClz);
		for (int i = 0, k = 0; i < params.length; i++) {
			if (ActionContext.class.isInstance(getArgTypes()[i])) {

				params[i] = null;
				continue;
			}
			if (argObj == null) {
				params[i] = null;
				continue;
			}
			Field f = getFields()[k++];
			params[i] = f.get(argObj);
		}

		return BizExcutor.exec(m, obj, params, null);
	}

	/**
	 * 按参数顺序进行RPC调用
	 * 
	 * @param args
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Object invokeByOrder(String... args) throws Throwable {
		if (argTypes == null || argTypes.length == 0) {
			return BizExcutor.exec(m, obj, null, null);
		}
		Object[] params = new Object[getArgTypes().length];
		if (getArgClz() == null) {

			return null;
		}
		if (args == null || args.length == 0) {
			SumkException.throwException(12012, m.getName() + "的参数不能为空");
		}
		if (args.length != argTypes.length) {
			SumkException.throwException(12013,
					m.getName() + "需要传递的参数有" + argTypes.length + "个，实际传递的是" + args.length + "个");
		}
		for (int i = 0, k = 0; i < params.length; i++) {
			if (ActionContext.class.isInstance(getArgTypes()[i])) {

				params[i] = null;
				continue;
			}
			if (args[k] == null) {
				params[i] = null;
				continue;
			}
			Field f = fields[k];
			params[i] = GsonUtil.fromJson(args[i], f.getGenericType());
			k++;
		}

		return BizExcutor.exec(m, obj, params, null);
	}

}
