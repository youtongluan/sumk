package org.yx.bean.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AopExecutorManager {

	private final AopExecutorSupplier[] baseSuppliers;

	private List<AopContext[]> aopContexts = new ArrayList<>();

	public AopExecutorManager(List<AopExecutorSupplier> advisors) {
		advisors.sort(null);
		this.baseSuppliers = advisors.toArray(new AopExecutorSupplier[advisors.size()]);
	}

	public AopExecutorChain getChain(int index) {
		AopContext[] aopContexts = this.aopContexts.get(index);
		AopExecutor[] excutors = new AopExecutor[aopContexts.length];
		for (int i = 0; i < aopContexts.length; i++) {
			excutors[i] = aopContexts[i].getAopExecutor();
		}
		return new AopExecutorChain(excutors);
	}

	public List<AopContext> willProxyExcutorSuppliers(Class<?> clz, Method method) {
		List<AopContext> list = new ArrayList<>(baseSuppliers.length);
		for (int i = 0; i < baseSuppliers.length; i++) {
			AopExecutorSupplier supplier = this.baseSuppliers[i];
			Object attach = supplier.willProxy(clz, method);
			if (attach != null) {
				list.add(new AopContext(supplier, attach));
			}
		}
		return list;
	}

	public int indexSupplier(List<AopContext> supplierList) {
		AopContext[] supplier = supplierList.toArray(new AopContext[supplierList.size()]);
		int currentLength = this.aopContexts.size();
		this.aopContexts.add(supplier);
		return currentLength;
	}

}
