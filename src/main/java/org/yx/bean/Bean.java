package org.yx.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示是IOC中的bean。使用在类上面，不支持接口注解 SOA、Web注释，包含了Bean的功能
 * 
 * @author 游夏
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
	String value() default "";

	Proxy proxy() default Proxy.CONFIG;
}
