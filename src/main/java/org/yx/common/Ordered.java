package org.yx.common;

public interface Ordered extends Comparable<Ordered> {
	/**
	 * 升序，值越大，优先级越低。一般不采用负数
	 * 
	 * @return
	 */
	default int order() {
		return 100;
	}

	@Override
	default int compareTo(Ordered o) {
		return this.order() - o.order();
	}

}
