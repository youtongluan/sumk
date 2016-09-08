package org.yx.rpc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.yx.bean.Bean;
import org.yx.db.DBType;

/**
 * 声明在方法上，表示该方法是个接口。如果接口名没有显示指定。接口名就是方法名。
 * 
 * @author youxia
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Bean
public @interface SOA  {
	/**
	 * 服务名称，如果为空，就根据方法名获取
	 * @return
	 */
	String value() default "";

	public String description() default "";
	
	public DBType dbType() default DBType.READONLY;
	public String dbName() default "";

}
