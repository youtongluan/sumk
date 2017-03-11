package org.yx.validate;

import org.yx.bean.Bean;
import org.yx.exception.InvalidParamException;

/**
 * 用于做非空验证
 * 
 * @author youxia
 * 
 */
@Bean
public class NotNullValidator implements Validator {

	@Override
	public void valid(ParamInfo param, Object arg) throws InvalidParamException {
		if (param.param.required() && arg == null) {
			throw new InvalidParamException("#必填", param, arg);
		}

	}

}
