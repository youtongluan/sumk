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
package org.yx.common.validate;

import org.yx.conf.AppInfo;
import org.yx.util.StringUtil;

/**
 * 这个异常表示是参数验证失败
 * 
 * @author 游夏
 *
 */
public class InvalidParamException extends Exception {

	private static final long serialVersionUID = 125465546L;

	private ParameterInfo info;

	public InvalidParamException(String message, ParameterInfo info) {
		super(message);
		this.info = info;
	}

	@Override
	public String getMessage() {
		String ret = super.getMessage();
		if (ret.indexOf("#") < 0) {
			return ret;
		}
		if (info == null) {
			return ret.replace("#", "参数");
		}
		if (AppInfo.getBoolean("sumk.valid.name.cn", true) && StringUtil.isNotEmpty(info.getCnName())) {
			return ret.replace("#", info.getCnName());
		}
		if (AppInfo.getBoolean("sumk.valid.name.raw", true)) {
			return ret.replace("#", info.getParamName());
		}
		return ret.replace("#", "参数");
	}

	public ParameterInfo getInfo() {
		return info;
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
