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

import org.yx.bean.aop.asm.MethodPojo;
import org.yx.bean.aop.asm.ParamPojo;
import org.yx.bean.aop.context.CalleeNode;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.exception.BizException;
import org.yx.exception.SumkException;
import org.yx.http.HttpErrorCode;
import org.yx.http.HttpJson;
import org.yx.http.MessageType;
import org.yx.http.kit.HttpSettings;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.http.spec.HttpSpecs;
import org.yx.http.spec.UploadSpec;
import org.yx.http.spec.WebSpec;
import org.yx.log.Logs;
import org.yx.util.CollectionUtil;

public final class HttpActionNode extends CalleeNode {

	private final boolean requireLogin;
	private final MessageType requestType;
	private final boolean sign;
	private final MessageType responseType;
	private final List<String> httpMethod;
	private final List<String> tags;
	private final String cnName;
	private final UploadSpec upload;

	public UploadSpec upload() {
		return this.upload;
	}

	public ParamPojo buildParamPojo(Object reqData) throws Exception {
		if (this.params.paramLength() == 0 || reqData == null) {
			return createEmptyParamObj();
		}
		Class<? extends ParamPojo> argClz = this.params.paramClz();
		if (reqData.getClass() != String.class) {
			if (argClz.isInstance(reqData)) {
				return argClz.cast(reqData);
			}
			throw new SumkException(1245464, "http argument " + reqData.getClass().getName() + " is not String");
		}
		String data = (String) reqData;
		if (data.isEmpty()) {
			return createEmptyParamObj();
		}
		try {
			return HttpJson.operator().fromJson(data, argClz);
		} catch (Exception e) {
			Logs.http().warn("json解析异常", e);
			throw BizException.create(HttpErrorCode.DATA_FORMAT_ERROR, "数据格式错误");
		}
	}

	private MessageType getMessageType(MessageType configed, String key) {
		if (configed != MessageType.DEFAULT) {
			return configed;
		}
		return InnerHttpUtil.parseMessageType(AppInfo.get(key));
	}

	private List<String> httpMethod(WebSpec web) {
		String[] m = web.method();
		if (m.length > 0) {
			return CollectionUtil.unmodifyList(m);
		}
		return HttpSettings.defaultHttpMethods();
	}

	public HttpActionNode(Object obj, Method method, MethodPojo argClzInfo, WebSpec action) {
		super(obj, method, argClzInfo, Objects.requireNonNull(action).toplimit() > 0 ? action.toplimit()
				: AppInfo.getInt("sumk.http.toplimit.default", Const.DEFAULT_TOPLIMIT));
		this.cnName = action.cnName();
		this.httpMethod = httpMethod(action);
		this.requestType = this.params.paramLength() == 0 ? MessageType.PLAIN
				: getMessageType(action.requestType(), "sumk.http.request.type");
		this.responseType = getMessageType(action.responseType(), "sumk.http.response.type");
		this.requireLogin = (action.requireLogin() && AppInfo.getBoolean("sumk.http.login.enable", false))
				|| action.requestType().isEncrypt() || action.responseType().isEncrypt();
		this.sign = this.params.paramLength() != 0 && action.sign()
				&& AppInfo.getBoolean("sumk.http.sign.enable", true);
		this.tags = CollectionUtil.unmodifyList(action.tags());
		this.upload = HttpSpecs.extractUpload(obj, method);
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

	/**
	 * 如果没有入参，那么它一定是PLAIN类型
	 * 
	 * @return 请求体类型
	 */
	public MessageType requestType() {
		return requestType;
	}

	/**
	 * 如果没有入参，那么它就一定是false
	 * 
	 * @return 参数签名
	 */
	public boolean sign() {
		return sign;
	}

	public MessageType responseType() {
		return responseType;
	}

	public boolean acceptMethod(String httpMethod) {
		return this.httpMethod.contains(httpMethod);
	}

	public List<String> tags() {
		return this.tags;
	}

	public List<String> methods() {
		return this.httpMethod;
	}

	public String cnName() {
		return cnName;
	}
}
