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
package org.yx.annotation.spec.parse;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.yx.annotation.spec.BeanSpec;
import org.yx.annotation.spec.BoxSpec;
import org.yx.annotation.spec.ColumnSpec;
import org.yx.annotation.spec.InjectSpec;
import org.yx.annotation.spec.ParamSpec;
import org.yx.annotation.spec.SoaClassSpec;
import org.yx.annotation.spec.SoaClientConfigSpec;
import org.yx.annotation.spec.SoaSpec;
import org.yx.annotation.spec.SumkFilterSpec;
import org.yx.annotation.spec.SumkServletSpec;
import org.yx.annotation.spec.TableSpec;
import org.yx.annotation.spec.UploadSpec;
import org.yx.annotation.spec.WebSpec;

public final class SpecParsers {

	private static Function<Class<?>, TableSpec> tableParser = BuiltIn.TABLE_PARSER;
	private static BiFunction<Class<?>, Field, ColumnSpec> columnParser = BuiltIn.COLUMN_PARSER;

	private static Function<Method, BoxSpec> boxParser = BuiltIn.BOX_PARSER;
	private static Function<Class<?>, BeanSpec> beanParser = BuiltIn.BEAN_PARSER;
	private static BiFunction<Object, Field, InjectSpec> injectParser = BuiltIn.INJECT_PARSER;

	private static Function<Field, ParamSpec> paramFieldParser = BuiltIn.PARAM_FIELD_PARSER;

	private static Function<Method, ParamSpec[]> paramParamterParser = BuiltIn.PARAM_PARAMTER_PARSER;

	private static BiFunction<Object, Method, WebSpec> webParser = BuiltIn.WEB_PARSER;
	private static BiFunction<Object, Method, UploadSpec> uploadParser = BuiltIn.UPLOAD_PARSER;
	private static Function<Class<?>, SumkFilterSpec> sumkFilterParser = BuiltIn.SUMK_FILTER_PARSER;
	private static Function<Class<?>, SumkServletSpec> sumkServletParser = BuiltIn.SUMK_SERVLET_PARSER;

	private static Function<Method, SoaSpec> soaParser = BuiltIn.SOA_PARSER;
	private static Function<Class<?>, SoaClassSpec> soaClassParser = BuiltIn.SOA_CLASS_PARSER;
	private static Function<Method, SoaClientConfigSpec> soaClientConfigParser = BuiltIn.SOA_CLIENT_CONFIG_PARSER;

	public static Function<Class<?>, BeanSpec> getBeanParser() {
		return beanParser;
	}

	public static Function<Method, BoxSpec> getBoxParser() {
		return boxParser;
	}

	public static BiFunction<Class<?>, Field, ColumnSpec> getColumnParser() {
		return columnParser;
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

	public static Function<Class<?>, SoaClassSpec> getSoaClassParser() {
		return soaClassParser;
	}

	public static Function<Method, SoaClientConfigSpec> getSoaClientConfigParser() {
		return soaClientConfigParser;
	}

	public static Function<Method, SoaSpec> getSoaParser() {
		return soaParser;
	}

	public static Function<Class<?>, SumkFilterSpec> getSumkFilterParser() {
		return sumkFilterParser;
	}

	public static Function<Class<?>, SumkServletSpec> getSumkServletParser() {
		return sumkServletParser;
	}

	public static Function<Class<?>, TableSpec> getTableParser() {
		return tableParser;
	}

	public static BiFunction<Object, Method, UploadSpec> getUploadParser() {
		return uploadParser;
	}

	public static BiFunction<Object, Method, WebSpec> getWebParser() {
		return webParser;
	}

	public static <T, R> R parse(T t, Function<T, R> parser) {
		return parser.apply(t);
	}

	public static <T, U, R> R parse(T t, U u, BiFunction<T, U, R> parser) {
		return parser.apply(t, u);
	}

	public static void setBeanParser(Function<Class<?>, BeanSpec> beanParser) {
		SpecParsers.beanParser = Objects.requireNonNull(beanParser);
	}

	public static void setBoxParser(Function<Method, BoxSpec> boxParser) {
		SpecParsers.boxParser = Objects.requireNonNull(boxParser);
	}

	public static void setColumnParser(BiFunction<Class<?>, Field, ColumnSpec> columnParser) {
		SpecParsers.columnParser = Objects.requireNonNull(columnParser);
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

	public static void setSoaClassParser(Function<Class<?>, SoaClassSpec> soaClassParser) {
		SpecParsers.soaClassParser = Objects.requireNonNull(soaClassParser);
	}

	public static void setSoaClientConfigParser(Function<Method, SoaClientConfigSpec> soaClientConfigParser) {
		SpecParsers.soaClientConfigParser = Objects.requireNonNull(soaClientConfigParser);
	}

	public static void setSoaParser(Function<Method, SoaSpec> soaParser) {
		SpecParsers.soaParser = Objects.requireNonNull(soaParser);
	}

	public static void setSumkFilterParser(Function<Class<?>, SumkFilterSpec> sumkFilterParser) {
		SpecParsers.sumkFilterParser = Objects.requireNonNull(sumkFilterParser);
	}

	public static void setSumkServletParser(Function<Class<?>, SumkServletSpec> sumkServletParser) {
		SpecParsers.sumkServletParser = Objects.requireNonNull(sumkServletParser);
	}

	public static void setTableParser(Function<Class<?>, TableSpec> tableParser) {
		SpecParsers.tableParser = Objects.requireNonNull(tableParser);
	}

	public static void setUploadParser(BiFunction<Object, Method, UploadSpec> uploadParser) {
		SpecParsers.uploadParser = Objects.requireNonNull(uploadParser);
	}

	public static void setWebParser(BiFunction<Object, Method, WebSpec> webParser) {
		SpecParsers.webParser = Objects.requireNonNull(webParser);
	}

}
