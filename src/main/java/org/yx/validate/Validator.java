package org.yx.validate;

import org.yx.common.Ordered;
import org.yx.exception.InvalidParamException;

public interface Validator extends Ordered {

	/**
	 * 参数检查，如果检查失败，就抛出InvalidException<BR>
	 * 实现类需要该param是否需要验证
	 * 
	 * @throws InvalidParamException
	 * 
	 */
	void valid(ParamInfo info, Object arg) throws InvalidParamException;
}
