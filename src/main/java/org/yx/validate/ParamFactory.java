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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ParamFactory {

	public static Param[] create(Method m) {
		Annotation[][] paramAnno = m.getParameterAnnotations();
		Param[] params = new Param[paramAnno.length];
		for (int i = 0; i < paramAnno.length; i++) {
			Annotation[] a = paramAnno[i];
			for (Annotation a2 : a) {
				if (Param.class == a2.annotationType()) {
					params[i] = (Param) a2;
					break;
				}
			}
		}
		return params;
	}
}
