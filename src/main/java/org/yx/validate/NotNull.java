package org.yx.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNull {

	/**
	 * 必传参数，不允许为null
	 */
	boolean required() default false;

	/**
	 * 字符串的最大长度
	 * 
	 * @return
	 */
	int maxLength() default -1;

	/**
	 * 字符串的最小长度
	 * 
	 * @return
	 */
	int minLength() default -1;

	/**
	 * 字符串长度
	 * 
	 * @return
	 */
	int length() default -1;
}
