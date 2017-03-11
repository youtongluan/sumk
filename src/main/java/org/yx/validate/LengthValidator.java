package org.yx.validate;

import org.yx.bean.Bean;
import org.yx.exception.InvalidParamException;

/**
 * 用于做长度验证
 * 
 * @author youxia
 * 
 */
@Bean
public class LengthValidator implements Validator {

	@Override
	public void valid(ParamInfo info, Object arg) throws InvalidParamException {
		Param param = info.param;
		if (param.length() < 0 || !String.class.isInstance(arg)) {
			return;
		}
		if (param.length() < 1) {
			return;
		}
		if (((String) arg).length() != param.length()) {
			throw new InvalidParamException("#长度不正确", info, arg);
		}

	}

}
