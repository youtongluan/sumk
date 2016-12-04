package org.yx.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 有这个标志，就说明是软删除
 * 
 * @author 游夏
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SoftDelete {
	/**
	 * 数据库中字段的名字
	 */
	String value();

	/**
	 * 只能是String、Int、Byte、Short、Long
	 * 
	 * @return
	 */
	Class<?> columnType() default String.class;

	/**
	 * 如果是数字类型，会被转化成数字类型
	 * 
	 * @return
	 */
	String validValue() default "1";

	/**
	 * 如果是数字类型，会被转化成数字类型
	 * 
	 * @return
	 */
	String inValidValue() default "0";
}
