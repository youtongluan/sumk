package org.yx.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.yx.bean.Bean;
import org.yx.db.DBType;

/**
 * 注入到spring中的bean。 被它所注解的类，要符合下列条件之一：
 * <LI>实现BizService接口
 * <LI>有且只有一个包名不是以java开头的接口
 * 
 * 
 * @author youxia
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Bean
public @interface SpringBean {
	/**
	 * 服务名称，如果为空，就根据方法名获取
	 * 
	 * @return
	 */
	String value() default "";

	public DBType dbType() default DBType.ANY;

	public String dbName() default "";
}
