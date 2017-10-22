package org.yx.validate;

import org.yx.bean.Bean;
import org.yx.exception.InvalidParamException;

@Bean
public class MaxLengthValidator implements Validator {

	@Override
	public void valid(ParamInfo info, Object arg) throws InvalidParamException {
		Param param = info.param;
		if (param.maxLength() < 0) {
			return;
		}
		if (!String.class.isInstance(arg)) {
			return;
		}
		if (((String) arg).length() > param.maxLength()) {
			throw new InvalidParamException("#长度超过" + param.maxLength(), info, arg);
		}

	}

}
