package org.yx.bean;

/**
 * 它的equal和hashcode都取决于它所代理的bean
 * 
 * @author 游夏
 *
 */
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
