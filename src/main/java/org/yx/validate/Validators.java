package org.yx.validate;

import java.util.List;

import org.yx.bean.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.exception.InvalidParamException;

@Bean
public class Validators implements Plugin {

	private static Validator[] validators;

	/**
	 * 非空验证
	 * 
	 * @param argName
	 * @param arg
	 * 
	 * @author youxia
	 * @throws InvalidParamException
	 */
	public static void check(ParamInfo info, Object arg) throws InvalidParamException {
		if (info == null || info.param == null || validators == null || validators.length == 0) {
			return;
		}
		for (Validator v : validators) {
			v.valid(info, arg);
		}
	}

	@Override
	public void start() {

		if (validators != null) {
			return;
		}
		List<Validator> list = IOC.getBeans(Validator.class);
		if (list == null || list.isEmpty()) {
			return;
		}
		validators = list.toArray(new Validator[list.size()]);
	}

	@Override
	public void stop() {

	}

}
