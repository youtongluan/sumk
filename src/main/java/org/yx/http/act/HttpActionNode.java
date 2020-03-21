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
package org.yx.http.act;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import org.yx.annotation.ErrorHandler;
import org.yx.annotation.Param;
import org.yx.annotation.http.RequestBody;
import org.yx.annotation.http.Upload;
import org.yx.annotation.http.Web;
import org.yx.asm.ArgPojo;
import org.yx.bean.Loader;
import org.yx.common.CalleeNode;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.http.HttpGson;
import org.yx.http.kit.HttpSettings;

public final class HttpActionNode extends CalleeNode {

	public final Web action;
	public final Upload upload;

	public final ErrorHandler errorHandler;

	private final Field requestBodyField;

	public boolean isRequestBody() {
		return requestBodyField != null;
	}

	public ArgPojo buildArgPojo(Object reqData) throws Exception {
		if (argNames.length == 0 || reqData == null) {
			return getEmptyArgObj();
		}
		if (this.isRequestBody()) {
			if (reqData.getClass() != byte[].class) {
				SumkException.throwException(34526965, "RequestBody的参数只能是byte[]");
			}
			ArgPojo arg = Loader.newInstance(this.argClz);
			this.requestBodyField.set(arg, reqData);
			return arg;
		}
		if (reqData.getClass() != String.class) {
			SumkException.throwException(1245464, "http argument " + reqData.getClass().getName() + " is not String");
		}
		String data = (String) reqData;
		if (data.isEmpty()) {
			return getEmptyArgObj();
		}
		return HttpGson.gson().fromJson(data, argClz);
	}

	public HttpActionNode(Object obj, Method method, Class<? extends ArgPojo> argClz, String[] argNames, Param[] params,
			Web action) {
		super(obj, method, argClz, argNames, params, Objects.requireNonNull(action).toplimit() > 0 ? action.toplimit()
				: AppInfo.getInt("sumk.http.thread.priority.default", 100000));
		this.action = action;
		if (HttpSettings.isUploadEnable()) {
			this.upload = this.getAnnotation(Upload.class);
		} else {
			this.upload = null;
		}
		if (this.getAnnotation(RequestBody.class) != null) {
			Field[] fs = argClz.getFields();
			if (fs == null || fs.length != 1) {
				SumkException.throwException(234135,
						method.getDeclaringClass().getName() + "的" + method.getName() + "方法参数不是一个");
			}
			this.requestBodyField = fs[0];
			Class<?> bodyType = requestBodyField.getType();
			if (bodyType != byte[].class) {
				SumkException.throwException(2234125, method.getDeclaringClass().getName() + "的" + method.getName()
						+ "方法参数不是byte[]类型，@RequestBody只支持byte[]类型参数");
			}
		} else {
			this.requestBodyField = null;
		}
		this.errorHandler = this.getAnnotation(ErrorHandler.class);
	}

}
