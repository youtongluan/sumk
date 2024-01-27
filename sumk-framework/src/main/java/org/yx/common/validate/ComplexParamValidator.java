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

import java.lang.reflect.Array;
import java.util.List;

import org.yx.annotation.Bean;
import org.yx.exception.SimpleSumkException;
import org.yx.log.Logs;

@Bean
public class ComplexParamValidator implements Validator {

	@Override
	public void valid(final ParameterInfo info, Object arg) throws InvalidParamException {
		if (info == null || arg == null) {
			return;
		}
		Class<?> clz = arg.getClass();
		if (clz.isArray()) {
			int length = Array.getLength(arg);
			if (length == 0) {
				return;
			}
			for (int i = 0; i < length; i++) {
				Validators.check(info, Array.get(arg, i));
			}
			return;
		}
		if (!info.isComplex()) {
			return;
		}
		this.checkFields(FieldParameterHolder.get(clz), arg);
	}

	protected void checkFields(List<FieldParameterInfo> infos, Object obj) throws InvalidParamException {
		if (infos == null) {
			return;
		}
		try {
			for (FieldParameterInfo info : infos) {
				Validators.check(info, info.field.get(obj));
			}
		} catch (Exception e) {
			if (e instanceof InvalidParamException) {
				throw (InvalidParamException) e;
			}
			Logs.system().warn("参数校验发生异常，" + e.getLocalizedMessage(), e);
			throw new SimpleSumkException(5346614, "校验时发生异常，异常信息为" + e.getLocalizedMessage());
		}
	}

	@Override
	public int order() {
		return 10000;
	}

}
