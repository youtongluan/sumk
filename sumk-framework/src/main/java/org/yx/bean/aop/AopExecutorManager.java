package org.yx.bean.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.yx.bean.IOC;

public class AopExecutorManager {

	private static AopExecutorManager INSTANCE = new AopExecutorManager();

	public static AopExecutorManager get() {
		return INSTANCE;
	}

	public static void reset() {
		INSTANCE = new AopExecutorManager();
	}

	private List<AopContext[]> aopContexts = new ArrayList<>();

	public AopExecutorChain getChain(int index) {
		AopContext[] aopContexts = this.aopContexts.get(index);
		AopExecutor[] excutors = new AopExecutor[aopContexts.length];
		for (int i = 0; i < aopContexts.length; i++) {
			excutors[i] = aopContexts[i].getAopExecutor();
		}
		return new AopExecutorChain(excutors);
	}

	public List<AopContext> willProxyExcutorSuppliers(Class<?> clz, Method method) {
		List<AopExecutorSupplier> baseSuppliers = IOC.getBeans(AopExecutorSupplier.class);
		if (baseSuppliers.isEmpty()) {
			return Collections.emptyList();
		}
		List<AopContext> list = new ArrayList<>(baseSuppliers.size());
		for (AopExecutorSupplier supplier : baseSuppliers) {
			Object attach = supplier.willProxy(clz, method);
			if (attach != null) {
				list.add(new AopContext(supplier, attach));
			}
		}
		return list;
	}

	public synchronized int indexSupplier(List<AopContext> supplierList) {
		AopContext[] supplier = supplierList.toArray(new AopContext[supplierList.size()]);
		int currentLength = this.aopContexts.size();
		this.aopContexts.add(supplier);
		return currentLength;
	}

}
