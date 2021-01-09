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
package org.yx.bean;

public class BeanWrapper {

	private Object bean;

	BeanWrapper(Object bean) {
		this.bean = bean;
	}

	public Object getBean() {
		return bean;
	}

	public boolean isProxy() {
		return bean instanceof Boxed;
	}

	public Class<?> getTargetClass() {
		return isProxy() ? Boxed.class.cast(bean).targetRawClass() : bean.getClass();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bean == null) ? 0 : bean.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeanWrapper other = (BeanWrapper) obj;
		if (bean == null) {
			if (other.bean != null)
				return false;
		} else if (!bean.equals(other.bean))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(bean);
	}

}
