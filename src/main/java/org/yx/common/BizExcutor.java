/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.common;

import java.lang.reflect.InvocationTargetException;

import org.yx.asm.ArgPojo;
import org.yx.validate.ParamInfo;
import org.yx.validate.Validators;

public abstract class BizExcutor {

	public static Object exec(ArgPojo argObj, Object owner, Object[] params, ParamInfo[] infos) throws Throwable {
		if (infos != null) {
			for (int i = 0; i < infos.length; i++) {
				ParamInfo info = infos[i];
				if (info != null) {
					Validators.check(info, params[i]);
				}
			}
		}
		try {
			return argObj.invoke(owner);
		} catch (Throwable e) {

			if (InvocationTargetException.class.isInstance(e)) {
				InvocationTargetException te = (InvocationTargetException) e;
				if (te.getTargetException() != null) {
					throw te.getTargetException();
				}
			}
			throw e;
		}
	}

}
