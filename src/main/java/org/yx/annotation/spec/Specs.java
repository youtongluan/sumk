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

import static org.yx.annotation.spec.parse.SpecParsers.parse;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.yx.annotation.spec.parse.SpecParsers;

public final class Specs {

	public static TableSpec extractTable(Class<?> clz) {
		return parse(clz, SpecParsers.getTableParser());
	}

	public static ColumnSpec extractColumn(Class<?> clz, Field f) {
		return parse(clz, f, SpecParsers.getColumnParser());
	}

	public static BoxSpec extractBox(Method m) {
		return parse(m, SpecParsers.getBoxParser());
	}

	public static BeanSpec extractBean(Class<?> clz) {
		return parse(clz, SpecParsers.getBeanParser());
	}

	public static InjectSpec extractInject(Object destBean, Field f) {
		return parse(destBean, f, SpecParsers.getInjectParser());
	}

	public static ParamSpec extractParamField(Field f) {
		return parse(f, SpecParsers.getParamFieldParser());
	}

	public static ParamSpec[] extractParamParamter(Method m) {
		return parse(m, SpecParsers.getParamParamterParser());
	}

	public static WebSpec extractWeb(Object bean, Method m) {
		return parse(bean, m, SpecParsers.getWebParser());
	}

	public static UploadSpec extractUpload(Object bean, Method m) {
		return parse(bean, m, SpecParsers.getUploadParser());
	}

	public static SoaSpec extractSoa(Method m) {
		return parse(m, SpecParsers.getSoaParser());
	}

	public static SoaClassSpec extractSoaClass(Class<?> clz) {
		return parse(clz, SpecParsers.getSoaClassParser());
	}

	public static SumkServletSpec extractSumkServlet(Class<?> clz) {
		return parse(clz, SpecParsers.getSumkServletParser());
	}

	public static SumkFilterSpec extractSumkFilter(Class<?> clz) {
		return parse(clz, SpecParsers.getSumkFilterParser());
	}

	public static SoaClientConfigSpec extractSoaClientConfig(Method m) {
		return parse(m, SpecParsers.getSoaClientConfigParser());
	}
}
