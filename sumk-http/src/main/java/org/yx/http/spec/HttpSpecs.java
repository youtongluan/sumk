package org.yx.http.spec;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.yx.annotation.http.SumkFilter;
import org.yx.annotation.http.SumkServlet;
import org.yx.annotation.http.Upload;
import org.yx.annotation.http.Web;
import org.yx.annotation.spec.Specs;

public class HttpSpecs extends Specs {

	private static BiFunction<Object, Method, WebSpec> webParser = (bean, m) -> {
		Web web = m.getAnnotation(Web.class);
		if (web == null) {
			return null;
		}
		return new WebSpec(web.value(), web.cnName(), web.requireLogin(), web.requestType(), web.sign(),
				web.responseType(), web.tags(), web.toplimit(), web.method());
	};

	private static BiFunction<Object, Method, UploadSpec> uploadParser = (bean, m) -> {
		Upload c = m.getAnnotation(Upload.class);
		if (c == null) {
			return null;
		}
		return new UploadSpec(c.paramName());
	};

	private static Function<Class<?>, SumkServletSpec> sumkServletParser = clz -> {
		SumkServlet f = clz.getAnnotation(SumkServlet.class);
		if (f == null) {
			return null;
		}
		return new SumkServletSpec(f.name(), f.path(), f.loadOnStartup(), f.asyncSupported(), f.appKey());
	};

	private static Function<Class<?>, SumkFilterSpec> sumkFilterParser = clz -> {
		SumkFilter f = clz.getAnnotation(SumkFilter.class);
		if (f == null) {
			return null;
		}
		return new SumkFilterSpec(f.name(), f.path(), f.dispatcherType(), f.isMatchAfter(), f.asyncSupported());
	};

	public static Function<Class<?>, SumkFilterSpec> getSumkFilterParser() {
		return sumkFilterParser;
	}

	public static Function<Class<?>, SumkServletSpec> getSumkServletParser() {
		return sumkServletParser;
	}

	public static BiFunction<Object, Method, UploadSpec> getUploadParser() {
		return uploadParser;
	}

	public static BiFunction<Object, Method, WebSpec> getWebParser() {
		return webParser;
	}

	public static void setSumkFilterParser(Function<Class<?>, SumkFilterSpec> sumkFilterParser) {
		HttpSpecs.sumkFilterParser = Objects.requireNonNull(sumkFilterParser);
	}

	public static void setSumkServletParser(Function<Class<?>, SumkServletSpec> sumkServletParser) {
		HttpSpecs.sumkServletParser = Objects.requireNonNull(sumkServletParser);
	}

	public static void setUploadParser(BiFunction<Object, Method, UploadSpec> uploadParser) {
		HttpSpecs.uploadParser = Objects.requireNonNull(uploadParser);
	}

	public static void setWebParser(BiFunction<Object, Method, WebSpec> webParser) {
		HttpSpecs.webParser = Objects.requireNonNull(webParser);
	}

	public static WebSpec extractWeb(Object bean, Method m) {
		return parse(bean, m, webParser);
	}

	public static UploadSpec extractUpload(Object bean, Method m) {
		return parse(bean, m, uploadParser);
	}

	public static SumkServletSpec extractSumkServlet(Class<?> clz) {
		return parse(clz, sumkServletParser);
	}

	public static SumkFilterSpec extractSumkFilter(Class<?> clz) {
		return parse(clz, sumkFilterParser);
	}
}
