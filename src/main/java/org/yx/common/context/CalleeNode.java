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
package org.yx.common.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import org.yx.annotation.Param;
import org.yx.annotation.doc.Comment;
import org.yx.asm.ArgPojo;
import org.yx.bean.Loader;
import org.yx.log.Log;
import org.yx.main.SumkThreadPool;
import org.yx.util.CollectionUtil;
import org.yx.validate.FieldParameterHolder;
import org.yx.validate.ParamInfo;
import org.yx.validate.Validators;

public abstract class CalleeNode {

	protected final String[] argNames;

	protected final ParamInfo[] paramInfos;

	protected final Object owner;

	protected final Class<? extends ArgPojo> argClz;

	private final int toplimit;

	protected final Method method;

	public CalleeNode(Object owner, Method method, Class<? extends ArgPojo> argClz, String[] argNames, int toplimit) {
		this.owner = Objects.requireNonNull(owner);
		this.argClz = Objects.requireNonNull(argClz);
		this.argNames = Objects.requireNonNull(argNames);
		this.method = Objects.requireNonNull(method);
		Param[] params = this.resolveParamAnnotation(method);
		this.paramInfos = params == null || params.length == 0 ? null : new ParamInfo[params.length];
		this.toplimit = toplimit;
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
		registerFieldInfos();
	}

	protected void registerFieldInfos() {
		if (this.paramInfos == null) {
			return;
		}
		for (ParamInfo pf : this.paramInfos) {
			if (pf == null) {
				continue;
			}
			FieldParameterHolder.registerFieldInfo(pf.getParamType());
		}
	}

	public boolean overflowThreshold() {
		if (this.toplimit < SumkThreadPool.executor().threshold()) {
			if (Log.get("sumk.thread").isDebugEnabled()) {
				String msg = new StringBuilder().append("[")
						.append(this.getClass().getSimpleName().replace("ActionNode", "")).append("] ")
						.append(this.method.getDeclaringClass().getSimpleName()).append(".")
						.append(this.method.getName()).append("() - priority=").append(toplimit)
						.append(" ,  threshold=").append(SumkThreadPool.executor().threshold()).toString();
				Log.get("sumk.thread").debug(msg);
			}
			return true;
		}
		return false;
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

	public Method rawMethod() {
		return this.method;
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return method.getAnnotation(annotationClass);
	}

	public Annotation[] getDeclaredAnnotations() {
		return method.getDeclaredAnnotations();
	}

	public Annotation[][] getParameterAnnotations() {
		return method.getParameterAnnotations();
	}

	public AnnotatedType getAnnotatedReturnType() {
		return method.getAnnotatedReturnType();
	}

	public int toplimit() {
		return this.toplimit;
	}

	public boolean isEmptyArgument() {
		return this.argNames.length == 0;
	}

	public Object owner() {
		return this.owner;
	}

	public Object execute(ArgPojo argObj) throws Throwable {
		if (this.paramInfos != null) {
			Object[] params = argObj.params();
			for (int i = 0; i < paramInfos.length; i++) {
				ParamInfo info = paramInfos[i];
				if (info != null) {
					Validators.check(info, params[i]);
				}
			}
		}
		try {
			return argObj.invoke(owner);
		} catch (Throwable e) {

			if (e instanceof InvocationTargetException) {
				InvocationTargetException te = (InvocationTargetException) e;
				if (te.getTargetException() != null) {
					throw te.getTargetException();
				}
			}
			throw e;
		}
	}

	public List<String> argNames() {
		return CollectionUtil.unmodifyList(argNames);
	}

	public List<ParamInfo> paramInfos() {
		return CollectionUtil.unmodifyList(paramInfos);
	}

	public Class<? extends ArgPojo> argClz() {
		return this.argClz;
	}

	public String comment() {
		Comment c = getAnnotation(Comment.class);
		return c == null ? "" : c.value();
	}

	private Param[] resolveParamAnnotation(Method m) {
		Annotation[][] paramAnno = m.getParameterAnnotations();
		Param[] params = new Param[paramAnno.length];
		for (int i = 0; i < paramAnno.length; i++) {
			Annotation[] a = paramAnno[i];
			for (Annotation a2 : a) {
				if (Param.class == a2.annotationType()) {
					params[i] = (Param) a2;
					break;
				}
			}
		}
		return params;
	}
}
