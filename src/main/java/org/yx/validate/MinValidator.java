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
package org.yx.validate;

import org.yx.annotation.Bean;
import org.yx.exception.InvalidParamException;
import org.yx.util.M;

@Bean
public class MinValidator implements Validator {

	@Override
	public void valid(ParamInfo info, Object arg) throws InvalidParamException {
		String msg = this.buildMessage(info.param.min(), arg);
		if (msg != null) {
			throw new InvalidParamException(msg, info);
		}
	}

	public String buildMessage(int expect, Object arg) {
		if (expect < 0 || arg == null) {
			return null;
		}
		Class<?> clz = arg.getClass();
		if (String.class == clz) {
			String s = (String) arg;
			if (s.length() < expect) {
				return M.get("sumk.valid.msg.minLength", "#的长度不能小于{0},实际却是{1}", expect, s.length());
			}
		}
		if (Number.class.isAssignableFrom(clz)) {
			long n = ((Number) arg).longValue();
			if (n < expect) {
				return M.get("sumk.valid.msg.min", "#的值不能小于{0},实际却是{1}", expect, arg);
			}
		}
		return null;
	}
}
