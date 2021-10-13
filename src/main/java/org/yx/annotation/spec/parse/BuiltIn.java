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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.yx.annotation.Bean;
import org.yx.annotation.Box;
import org.yx.annotation.ConditionOnProperty;
import org.yx.annotation.Inject;
import org.yx.annotation.Param;
import org.yx.annotation.db.Column;
import org.yx.annotation.db.Table;
import org.yx.annotation.http.SumkFilter;
import org.yx.annotation.http.SumkServlet;
import org.yx.annotation.http.Upload;
import org.yx.annotation.http.Web;
import org.yx.annotation.rpc.Soa;
import org.yx.annotation.rpc.SoaClass;
import org.yx.annotation.rpc.SoaClientConfig;
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
import org.yx.util.StringUtil;

public final class BuiltIn {

	public static final Function<Class<?>, TableSpec> TABLE_PARSER = clz -> {
		Table table = clz.getAnnotation(Table.class);
		if (table == null) {
			return null;
		}
		return new TableSpec(table.value(), table.duration(), table.preInCache(), table.maxHit(), table.cacheType());
	};

	public static final BiFunction<Class<?>, Field, ColumnSpec> COLUMN_PARSER = (clz, f) -> {
		Column c = f.getAnnotation(Column.class);
		if (c == null) {
			return null;
		}
		return new ColumnSpec(c.value(), c.type(), c.order());
	};

	public static final Function<Method, BoxSpec> BOX_PARSER = m -> {
		Box c = m.getAnnotation(Box.class);
		if (c == null) {
			return null;
		}
		return new BoxSpec(c.value(), c.dbType(), c.transaction());
	};

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
		return new InjectSpec(c.value(), c.allowEmpty());
	};

	public static final Function<Field, ParamSpec> PARAM_FIELD_PARSER = f -> {
		Param c = f.getAnnotation(Param.class);
		if (c == null) {
			return null;
		}
		return SpecFactory.create(c);
	};

	public static final Function<Method, ParamSpec[]> PARAM_PARAMTER_PARSER = m -> {
		Annotation[][] paramAnno = m.getParameterAnnotations();
		ParamSpec[] params = new ParamSpec[paramAnno.length];
		for (int i = 0; i < paramAnno.length; i++) {
			Annotation[] a = paramAnno[i];
			for (Annotation a2 : a) {
				if (Param.class == a2.annotationType()) {
					params[i] = SpecFactory.create((Param) a2);
					break;
				}
			}
		}
		return params;
	};

	public static final BiFunction<Object, Method, WebSpec> WEB_PARSER = (bean, m) -> {
		Web c = m.getAnnotation(Web.class);
		if (c == null) {
			return null;
		}
		return SpecFactory.create(c);
	};

	public static final BiFunction<Object, Method, UploadSpec> UPLOAD_PARSER = (bean, m) -> {
		Upload c = m.getAnnotation(Upload.class);
		if (c == null) {
			return null;
		}
		return new UploadSpec(c.paramName());
	};

	public static final Function<Class<?>, SumkServletSpec> SUMK_SERVLET_PARSER = clz -> {
		SumkServlet c = clz.getAnnotation(SumkServlet.class);
		if (c == null) {
			return null;
		}
		return SpecFactory.create(c);
	};

	public static final Function<Class<?>, SumkFilterSpec> SUMK_FILTER_PARSER = clz -> {
		SumkFilter c = clz.getAnnotation(SumkFilter.class);
		if (c == null) {
			return null;
		}
		return SpecFactory.create(c);
	};

	public static final Function<Method, SoaSpec> SOA_PARSER = m -> {
		Soa c = m.getAnnotation(Soa.class);
		if (c == null) {
			return null;
		}
		return SpecFactory.create(c);
	};

	public static final Function<Class<?>, SoaClassSpec> SOA_CLASS_PARSER = clz -> {
		SoaClass c = clz.getAnnotation(SoaClass.class);
		if (c == null) {
			return null;
		}
		return new SoaClassSpec(c.refer());
	};

	public static final Function<Method, SoaClientConfigSpec> SOA_CLIENT_CONFIG_PARSER = m -> {
		SoaClientConfig c = m.getAnnotation(SoaClientConfig.class);
		if (c == null) {
			return null;
		}
		return new SoaClientConfigSpec(c.timeout(), c.tryCount());
	};

}
