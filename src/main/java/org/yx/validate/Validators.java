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

import java.util.List;

import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.exception.InvalidParamException;

@Bean
public class Validators implements Plugin {

	private static Validator[] validators;

	public static void check(ParamInfo info, Object arg) throws InvalidParamException {
		Validator[] validators = Validators.validators;
		if (info == null || info.param == null || validators == null || validators.length == 0) {
			return;
		}
		for (Validator v : validators) {
			v.valid(info, arg);
		}
	}

	@Override
	public void startAsync() {
		List<Validator> list = IOC.getBeans(Validator.class);
		if (list == null || list.isEmpty()) {
			validators = null;
			return;
		}
		validators = list.toArray(new Validator[list.size()]);
	}

}
