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

import java.lang.reflect.Method;
import java.util.Objects;

import org.yx.annotation.Param;
import org.yx.asm.ArgPojo;
import org.yx.bean.Loader;
import org.yx.log.Log;
import org.yx.main.SumkThreadPool;
import org.yx.validate.ParamInfo;

public abstract class CalleeNode {
	public static interface Visitor {
		Object visit(CalleeNode info) throws Throwable;
	}

	public final String[] argNames;

	public final ParamInfo[] paramInfos;

	public final Object owner;

	public final Class<? extends ArgPojo> argClz;

	private final int priority;

	protected final Method method;

	public CalleeNode(Object owner, Method method, Class<? extends ArgPojo> argClz, String[] argNames, Param[] params,
			int priority) {
		this.owner = Objects.requireNonNull(owner);
		this.argClz = Objects.requireNonNull(argClz);
		this.argNames = Objects.requireNonNull(argNames);
		this.method = Objects.requireNonNull(method);
		this.paramInfos = params == null || params.length == 0 ? null : new ParamInfo[params.length];
		this.priority = priority;
		if (this.paramInfos != null) {
			Class<?>[] argTypes = this.getParameterTypes();
			for (int i = 0; i < this.paramInfos.length; i++) {
				Param p = params[i];
				if (p == null) {
					continue;
				}
				paramInfos[i] = new ParamInfo(p, argNames[i], argTypes[i]);
			}
		}
	}

	public Object accept(Visitor visitor) throws Throwable {
		if (this.priority < SumkThreadPool.executor().threshold()) {
			if (Log.get("sumk.thread").isDebugEnabled()) {
				String msg = new StringBuilder().append("[")
						.append(this.getClass().getSimpleName().replace("ActionNode", "")).append("] ")
						.append(this.method.getDeclaringClass().getSimpleName()).append(".")
						.append(this.method.getName()).append("() - priority=").append(priority)
						.append(" ,  threshold=").append(SumkThreadPool.executor().threshold()).toString();
				Log.get("sumk.thread").debug(msg);
			}
			throw SumkThreadPool.THREAD_THRESHOLD_OVER;
		}
		return visitor.visit(this);
	}

	public ArgPojo getEmptyArgObj() throws Exception {
		return Loader.newInstance(this.argClz);
	}

	public Class<?> getDeclaringClass() {
		return this.method.getDeclaringClass();
	}

	public Class<?> getReturnType() {
		return this.method.getReturnType();
	}

	public String getMethodName() {
		return this.method.getName();
	}

	public Class<?>[] getParameterTypes() {
		return method.getParameterTypes();
	}

}
