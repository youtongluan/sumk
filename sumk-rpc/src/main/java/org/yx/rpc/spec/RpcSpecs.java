package org.yx.rpc.spec;

import java.lang.reflect.Method;
import java.util.function.Function;

import org.yx.annotation.rpc.Soa;
import org.yx.annotation.rpc.SoaClass;
import org.yx.annotation.rpc.SoaClientConfig;
import org.yx.annotation.spec.Specs;

public class RpcSpecs extends Specs {

	private static Function<Method, SoaSpec> soaParser = m -> {
		Soa soa = m.getAnnotation(Soa.class);
		if (soa == null) {
			return null;
		}
		return new SoaSpec(soa.value(), soa.cnName(), soa.appIdPrefix(), soa.toplimit(), soa.publish());
	};

	private static Function<Class<?>, SoaClassSpec> soaClassParser = clz -> {
		SoaClass c = clz.getAnnotation(SoaClass.class);
		if (c == null) {
			return null;
		}
		return new SoaClassSpec(c.refer());
	};

	private static Function<Method, SoaClientConfigSpec> soaClientConfigParser = m -> {
		SoaClientConfig c = m.getAnnotation(SoaClientConfig.class);
		if (c == null) {
			return null;
		}
		return new SoaClientConfigSpec(c.timeout(), c.tryCount());
	};

	public static SoaSpec extractSoa(Method m) {
		return parse(m, soaParser);
	}

	public static SoaClassSpec extractSoaClass(Class<?> clz) {
		return parse(clz, soaClassParser);
	}

	public static SoaClientConfigSpec extractSoaClientConfig(Method m) {
		return parse(m, soaClientConfigParser);
	}

	public static Function<Method, SoaSpec> getSoaParser() {
		return soaParser;
	}

	public static void setSoaParser(Function<Method, SoaSpec> soaParser) {
		RpcSpecs.soaParser = soaParser;
	}

	public static Function<Class<?>, SoaClassSpec> getSoaClassParser() {
		return soaClassParser;
	}

	public static void setSoaClassParser(Function<Class<?>, SoaClassSpec> soaClassParser) {
		RpcSpecs.soaClassParser = soaClassParser;
	}

	public static Function<Method, SoaClientConfigSpec> getSoaClientConfigParser() {
		return soaClientConfigParser;
	}

	public static void setSoaClientConfigParser(Function<Method, SoaClientConfigSpec> soaClientConfigParser) {
		RpcSpecs.soaClientConfigParser = soaClientConfigParser;
	}

}
