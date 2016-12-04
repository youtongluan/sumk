package org.yx.bean;

public class BeanWrapper {

	private Object bean;

	private Class<?> targetClass;

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public boolean isProxy() {
		return bean.getClass() != this.targetClass;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((targetClass == null) ? 0 : targetClass.hashCode());
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
		if (targetClass == null) {
			if (other.targetClass != null)
				return false;
		} else if (!targetClass.equals(other.targetClass))
			return false;
		return true;
	}

}
