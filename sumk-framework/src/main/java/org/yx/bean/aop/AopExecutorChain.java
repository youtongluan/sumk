package org.yx.bean.aop;

import org.yx.log.Logs;
import org.yx.util.ExceptionUtil;

/**
 * 嵌套式执行，先执行before()的晚执行after()
 * 
 * @author youtl
 *
 */
public class AopExecutorChain {
	private final AopExecutor[] executors;
	private int lastExecutor;

	public AopExecutorChain(AopExecutor[] excutors) {
		this.executors = excutors;
	}

	public void before(Object[] params) throws Exception {
		for (int i = 0; i < executors.length; i++) {
			this.lastExecutor = i;
			AopExecutor excutor = executors[lastExecutor];
			excutor.before(params);
		}
	}

	public void after(Object result, Throwable e, boolean methodExecuted) {
		Throwable ex = e;
		for (int i = lastExecutor; i >= 0; i--) {
			try {
				ex = executors[i].after(result, ex, methodExecuted);
			} catch (Throwable e2) {
				Logs.aop().error("excpetion raised when after() in aopExecutor " + executors[i], e2);
				ex = e2;
			}
		}
		if (ex != null) {
			Logs.aop().error(ex.getMessage(), ex);
			throw ExceptionUtil.toRuntimeException(ex);
		}
	}

}
