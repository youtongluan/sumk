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
import java.util.List;
import java.util.Objects;

import org.yx.annotation.http.Upload;
import org.yx.annotation.http.Web;
import org.yx.asm.ArgPojo;
import org.yx.common.CalleeNode;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.http.HttpErrorCode;
import org.yx.http.HttpJson;
import org.yx.http.MessageType;
import org.yx.http.kit.HttpException;
import org.yx.log.Logs;
import org.yx.util.CollectionUtil;

import com.google.gson.JsonParseException;

public final class HttpActionNode extends CalleeNode {

	private final boolean requireLogin;
	private final MessageType requestType;
	private final boolean sign;
	private final MessageType responseType;
	private final String[] method;
	private final List<String> tags;

	public Web action() {
		return this.getAnnotation(Web.class);
	}

	public Upload upload() {
		return this.getAnnotation(Upload.class);
	}

	public ArgPojo buildArgPojo(Object reqData) throws Exception {
		if (this.isEmptyArgument() || reqData == null) {
			return getEmptyArgObj();
		}
		if (reqData.getClass() != String.class) {
			throw new SumkException(1245464, "http argument " + reqData.getClass().getName() + " is not String");
		}
		String data = (String) reqData;
		if (data.isEmpty()) {
			return getEmptyArgObj();
		}
		try {
			return HttpJson.operator().fromJson(data, argClz);
		} catch (JsonParseException e) {
			Logs.http().warn("json解析异常", e);
			throw HttpException.create(HttpErrorCode.DATA_FORMAT_ERROR, "数据格式错误");
		}
	}

	public HttpActionNode(Object obj, Method method, Class<? extends ArgPojo> argClz, String[] argNames, Web action) {
		super(obj, method, argClz, argNames, Objects.requireNonNull(action).toplimit() > 0 ? action.toplimit()
				: AppInfo.getInt("sumk.http.thread.priority.default", 100000));
		this.method = action.method();
		this.requestType = action.requestType();
		this.responseType = action.responseType();
		this.requireLogin = (action.requireLogin() && AppInfo.getBoolean("sumk.http.login.enable", false))
				|| action().requestType().isEncrypt() || action().responseType().isEncrypt();
		this.sign = action.sign() && AppInfo.getBoolean("sumk.http.sign.enable", true);
		this.tags = CollectionUtil.unmodifyList(action.tags());
	}

	/**
	 * 返回值受@Web、sumk.http.login.enable配置、requestType、responseType影响。<BR>
	 * 如果requestType、responseType中有一个为需要加密，它就返回true。
	 * 
	 * @return true表示接口需要登录后才能访问
	 */
	public boolean requireLogin() {
		return requireLogin;
	}

	public MessageType requestType() {
		return requestType;
	}

	public boolean sign() {
		return sign;
	}

	public MessageType responseType() {
		return responseType;
	}

	public boolean acceptMethod(String httpMethod) {
		for (String m : method) {
			if (m.equals(httpMethod)) {
				return true;
			}
		}
		return false;
	}

	public List<String> tags() {
		return this.tags;
	}

}
