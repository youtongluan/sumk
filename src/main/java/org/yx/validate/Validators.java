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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.yx.bean.IOC;
import org.yx.exception.InvalidParamException;
import org.yx.util.SumkDate;

public class Validators {

	private static Validator[] validators = new Validator[0];

	public static void check(ParameterInfo info, Object arg) throws InvalidParamException {
		if (info == null || !info.maybeCheck()) {
			return;
		}
		Validator[] validators = Validators.validators;
		for (Validator v : validators) {
			v.valid(info, arg);
		}
	}

	public static synchronized void init() {
		if (validators.length > 0) {
			return;
		}
		List<Validator> list = IOC.getBeans(Validator.class);
		if (list == null || list.isEmpty()) {
			return;
		}
		validators = list.toArray(new Validator[list.size()]);
	}

	public static boolean supportComplex(Class<?> clazz) {
		if (clazz.isArray()) {
			return supportComplex(clazz.getComponentType());
		}
		if (clazz.isPrimitive() || clazz.getName().startsWith("java.") || clazz == SumkDate.class
				|| Map.class.isAssignableFrom(clazz) || Collection.class.isAssignableFrom(clazz)) {
			return false;
		}
		return true;
	}

}
