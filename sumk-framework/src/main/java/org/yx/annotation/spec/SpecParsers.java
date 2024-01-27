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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class SpecParsers {

	private static Function<Class<?>, BeanSpec> beanParser = BuiltInParsers.BEAN_PARSER;
	private static BiFunction<Object, Field, InjectSpec> injectParser = BuiltInParsers.INJECT_PARSER;

	private static Function<Field, ParamSpec> paramFieldParser = BuiltInParsers.PARAM_FIELD_PARSER;

	private static Function<Method, ParamSpec[]> paramParamterParser = BuiltInParsers.PARAM_PARAMTER_PARSER;

	public static Function<Class<?>, BeanSpec> getBeanParser() {
		return beanParser;
	}

	public static BiFunction<Object, Field, InjectSpec> getInjectParser() {
		return injectParser;
	}

	public static Function<Field, ParamSpec> getParamFieldParser() {
		return paramFieldParser;
	}

	public static Function<Method, ParamSpec[]> getParamParamterParser() {
		return paramParamterParser;
	}

	public static void setBeanParser(Function<Class<?>, BeanSpec> beanParser) {
		SpecParsers.beanParser = Objects.requireNonNull(beanParser);
	}

	public static void setInjectParser(BiFunction<Object, Field, InjectSpec> injectParser) {
		SpecParsers.injectParser = Objects.requireNonNull(injectParser);
	}

	public static void setParamFieldParser(Function<Field, ParamSpec> paramFieldParser) {
		SpecParsers.paramFieldParser = Objects.requireNonNull(paramFieldParser);
	}

	public static void setParamParamterParser(Function<Method, ParamSpec[]> paramParamterParser) {
		SpecParsers.paramParamterParser = Objects.requireNonNull(paramParamterParser);
	}

}
