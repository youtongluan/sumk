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
package org.yx.exception;

import org.yx.util.StringUtil;
import org.yx.validate.ParamInfo;

/**
 * 这个异常表示是参数验证失败
 * 
 * @author 游夏
 *
 */
public class InvalidParamException extends Exception {

	private static final long serialVersionUID = 125465546L;

	private Object param;
	private ParamInfo info;

	public InvalidParamException(String message, ParamInfo info, Object arg) {
		super(message);
		this.param = arg;
		this.info = info;
	}

	@Override
	public String getMessage() {
		String ret = super.getMessage();
		if (ret.contains("#")) {
			return info != null && info.getParam() != null && StringUtil.isNotEmpty(info.getParam().cnName())
					? ret.replace("#", info.getParam().cnName()) : ret.replace("#", "参数");
		}
		return ret;
	}

	public Object getParam() {
		return param;
	}

	public ParamInfo getInfo() {
		return info;
	}

}
