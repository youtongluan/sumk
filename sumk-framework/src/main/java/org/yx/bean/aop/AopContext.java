package org.yx.bean.aop;

import java.util.Objects;

public class AopContext {
	private final AopExecutorSupplier supplier;
	private final Object attach;

	public AopContext(AopExecutorSupplier supplier, Object attach) {
		this.supplier = Objects.requireNonNull(supplier);
		this.attach = attach;
	}

	public AopExecutor getAopExecutor() {
		return this.supplier.get(attach);
	}

	public AopExecutorSupplier getSupplier() {
		return supplier;
	}

	public Object getAttach() {
		return attach;
	}

	@Override
	public int hashCode() {
		return Objects.hash(attach, supplier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AopContext other = (AopContext) obj;
		return Objects.equals(attach, other.attach) && Objects.equals(supplier, other.supplier);
	}

}
