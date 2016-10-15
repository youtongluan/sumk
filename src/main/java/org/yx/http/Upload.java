package org.yx.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明在方法上，表示该方法可以被http调用。
 * 
 * 
 * @author youxia
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Upload {

	int maxSize() default 1024 * 1024 * 10;

	int maxFiles() default 1;

	/**
	 * 小写格式
	 * 
	 * @return
	 */
	String[] exts() default { ".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".gif", ".png", ".jpg", ".jpeg",
			".bmp" };

	Store tempStore() default Store.VM;
}
