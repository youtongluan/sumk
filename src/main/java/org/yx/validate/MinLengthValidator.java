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
public class MinLengthValidator implements Validator {

	@Override
	public void valid(ParamInfo info, Object arg) throws InvalidParamException {
		Param param = info.param;
		if (param.minLength() < 0) {
			return;
		}
		if (arg == null) {
			throw new InvalidParamException("#设置了最小长度,不能为空", info, arg);
		}
		if (!String.class.isInstance(arg)) {
			return;
		}

		if (((String) arg).length() < param.minLength()) {
			throw new InvalidParamException("#长度小于" + param.minLength(), info, arg);
		}

	}

}
