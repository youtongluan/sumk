package org.yx.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.yx.db.DBType;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Box {

	String dbName();

	DBType dbType() default DBType.ANY;

	/**
	 * 是否支持事务传递
	 */
	boolean embed() default true;
}
