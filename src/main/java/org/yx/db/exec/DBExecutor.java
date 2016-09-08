package org.yx.db.exec;


@FunctionalInterface
public interface DBExecutor {
	/**
	 * 对session进行读写操作，不需要显示提交或回滚，更不能去关闭
	 * @param action 用于提交或回滚等操作
	 * @param container 用来包装返回值
	 * @return
	 * @throws Exception
	 */
	void exec(ExeContext container) throws Exception;
}
