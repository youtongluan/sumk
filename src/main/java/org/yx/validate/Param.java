package org.yx.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于rpc和http的参数验证
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {

	/**
	 * 中文名称
	 * 
	 * @return
	 */
	String cnName() default "";

	/**
	 * 必传参数，不允许为null
	 */
	boolean required() default false;

	/**
	 * 字符串的最大长度，小于0表示不限
	 * 
	 * @return
	 */
	int maxLength() default -1;

	/**
	 * 字符串的最小长度，小于0表示不限
	 * 
	 * @return
	 */
	int minLength() default -1;

	/**
	 * 字符串长度，小于0表示不限
	 * 
	 * @return
	 */
	int length() default -1;

	/**
	 * 用户自定义的属性。作用留给用户自己扩展
	 * 
	 * @return
	 */
	String custom() default "";
}
