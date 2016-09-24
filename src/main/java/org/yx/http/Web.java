package org.yx.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.yx.bean.Bean;
import org.yx.db.DBType;

/**
 * 声明在方法上，表示该方法可以被http调用。 如果对象的某个方法被Web注解，那么那个对象相当于被Bean注解
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
public @interface Web {
	/**
	 * 服务名称，如果为空，就根据方法名获取
	 * 
	 * @return
	 */
	String value() default "";

	public String description() default "";

	public DBType dbType() default DBType.ANY;

	public String dbName() default "";

	public boolean requireLogin() default false;

	public EncryptType requestEncrypt() default EncryptType.NONE;

	public boolean sign() default false;

	public EncryptType responseEncrypt() default EncryptType.NONE;

}
