package org.yx.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.yx.db.dao.ColumnType;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
	/**
	 * 数据库字段的名字，不填的话，就是属性名(大写)
	 * 
	 * @return
	 */
	String value() default "";

	ColumnType columnType() default ColumnType.NORMAL;

	byte columnOrder() default 1;
}
