package org.yx.bean;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.yx.annotation.spec.InjectSpec;
import org.yx.exception.SimpleSumkException;
import org.yx.util.CollectionUtil;

public class DefaultBeanFieldFinder implements BeanFieldFinder {

	@Override
	public Object findTarget(Field f, Object bean, InjectSpec inject) throws Exception {
		Class<?> fieldType = f.getType();
		if (fieldType.isArray()) {
			return getArrayField(f, bean, inject.allowEmpty());
		}
		if (List.class == fieldType || Collection.class == fieldType) {
			return getListField(f, bean, inject.allowEmpty());
		}
		return getBean(f);
	}

	protected Object getBean(Field f) {
		String name = f.getName();
		Class<?> clz = f.getType();

		List<?> list = InnerIOC.pool.getBeans(name, clz);
		if (list.size() == 1) {
			return list.get(0);
		}
		if (list.size() > 1) {
			for (Object obj : list) {

				if (clz == BeanKit.getTargetClass(obj)) {
					return obj;
				}
			}
		}
		return IOC.get(clz);
	}

	protected List<?> getListField(Field f, Object bean, boolean allowEmpty) throws ClassNotFoundException {
		String genericName = f.getGenericType().getTypeName();
		if (genericName == null || genericName.isEmpty() || !genericName.contains("<")) {
			throw new SimpleSumkException(-239845611,
					bean.getClass().getName() + "." + f.getName() + "is List,but not List<T>");
		}
		genericName = genericName.substring(genericName.indexOf("<") + 1, genericName.length() - 1);
		Class<?> clz = Loader.loadClassExactly(genericName);
		if (clz == Object.class) {
			throw new SimpleSumkException(-23984568,
					bean.getClass().getName() + "." + f.getName() + ": beanClz of @Inject in list type cannot be null");
		}
		List<?> target = IOC.getBeans(clz);
		if (target == null || target.isEmpty()) {
			if (!allowEmpty) {
				throw new SimpleSumkException(-235435652, bean.getClass().getName() + "." + f.getName() + " is empty.");
			}
			return Collections.emptyList();
		}
		return CollectionUtil.unmodifyList(target.toArray());
	}

	protected Object[] getArrayField(Field f, Object bean, boolean allowEmpty) {
		Class<?> clz = f.getType().getComponentType();
		List<?> target = IOC.getBeans(clz);
		if (target == null || target.isEmpty()) {
			if (!allowEmpty) {
				throw new SimpleSumkException(-235435651, bean.getClass().getName() + "." + f.getName() + " is empty.");
			}
			return (Object[]) Array.newInstance(clz, 0);
		}
		return target.toArray((Object[]) Array.newInstance(clz, target.size()));
	}

}
