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
package org.yx.annotation.spec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.yx.annotation.Bean;
import org.yx.annotation.ConditionOnProperty;
import org.yx.annotation.Inject;
import org.yx.annotation.Param;
import org.yx.util.StringUtil;

public final class BuiltInParsers {

	public static final Function<Class<?>, BeanSpec> BEAN_PARSER = clz -> {
		Bean c = clz.getAnnotation(Bean.class);
		if (c == null) {
			return null;
		}
		ConditionOnProperty p = clz.getAnnotation(ConditionOnProperty.class);
		if (p == null) {
			return new BeanSpec(c.value(), null, true);
		}
		return new BeanSpec(c.value(), StringUtil.toLatin(p.value()), p.onMatch());
	};

	public static final BiFunction<Object, Field, InjectSpec> INJECT_PARSER = (src, f) -> {
		Inject c = f.getAnnotation(Inject.class);
		if (c == null) {
			return null;
		}
		return new InjectSpec(c.value(), c.allowEmpty(), c.allowMulti());
	};

	public static final Function<Field, ParamSpec> PARAM_FIELD_PARSER = f -> {
		Param c = f.getAnnotation(Param.class);
		if (c == null) {
			return null;
		}
		return createParamSpec(c);
	};

	public static final Function<Method, ParamSpec[]> PARAM_PARAMTER_PARSER = m -> {
		Annotation[][] paramAnno = m.getParameterAnnotations();
		ParamSpec[] params = new ParamSpec[paramAnno.length];
		for (int i = 0; i < paramAnno.length; i++) {
			Annotation[] a = paramAnno[i];
			for (Annotation a2 : a) {
				if (Param.class == a2.annotationType()) {
					params[i] = createParamSpec((Param) a2);
					break;
				}
			}
		}
		return params;
	};

	public static ParamSpec createParamSpec(Param p) {
		return new ParamSpec(p.value(), p.required(), p.max(), p.min(), p.example(), p.comment(), p.complex(), null);
	}

}
