package org.test;

import java.util.List;

import org.junit.Test;
import org.yx.log.Log;
import org.yx.log.impl.DefaultUnionLog;
import org.yx.log.impl.UnionLogDao;
import org.yx.log.impl.UnionLogObject;
import org.yx.log.impl.UnionLogs;

public class UnionDemo {

	@Test
	public void test() throws InterruptedException {
		DefaultUnionLog unionLog=(DefaultUnionLog) UnionLogs.getUnionLog();
		unionLog.setDao(new UnionLogDao(){

			@Override
			public void flush(boolean idle) {
			}

			@Override
			public void store(List<UnionLogObject> logs) throws Exception {
				for(UnionLogObject log:logs){
					System.out.println(log.getLog());
				}
			}
		});
		UnionLogs.start();//启动统一日志
		
//		app.propertis中配置了unionTest写入到统一日志
		Log.get("unionTest").info("abc");
		Log.get("unionTest").info("222");
		Thread.sleep(1000);
	}

}
