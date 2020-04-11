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

import java.lang.reflect.Method;
import java.util.Objects;

import org.yx.annotation.ErrorHandler;
import org.yx.annotation.Param;
import org.yx.annotation.http.Upload;
import org.yx.annotation.http.Web;
import org.yx.asm.ArgPojo;
import org.yx.common.CalleeNode;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.http.HttpErrorCode;
import org.yx.http.HttpGson;
import org.yx.http.kit.HttpException;
import org.yx.http.kit.HttpSettings;
import org.yx.log.Logs;

import com.google.gson.JsonParseException;

public final class HttpActionNode extends CalleeNode {

	public final Web action;
	public final Upload upload;

	public final ErrorHandler errorHandler;

	public ArgPojo buildArgPojo(Object reqData) throws Exception {
		if (this.isEmptyArgument() || reqData == null) {
			return getEmptyArgObj();
		}
		if (reqData.getClass() != String.class) {
			SumkException.throwException(1245464, "http argument " + reqData.getClass().getName() + " is not String");
		}
		String data = (String) reqData;
		if (data.isEmpty()) {
			return getEmptyArgObj();
		}
		try {
			return HttpGson.gson().fromJson(data, argClz);
		} catch (JsonParseException e) {
			Logs.rpc().debug("json解析异常", e);
			throw HttpException.create(HttpErrorCode.DATA_FORMAT_ERROR, "数据格式错误");
		}
	}

	public HttpActionNode(Object obj, Method method, Class<? extends ArgPojo> argClz, String[] argNames, Param[] params,
			Web action) {
		super(obj, method, argClz, argNames, params, Objects.requireNonNull(action).toplimit() > 0 ? action.toplimit()
				: AppInfo.getInt("sumk.http.thread.priority.default", 100000));
		this.action = action;
		this.upload = HttpSettings.isUploadEnable() ? this.getAnnotation(Upload.class) : null;
		this.errorHandler = this.getAnnotation(ErrorHandler.class);
	}
}
