package org.test.orm;

import java.util.Arrays;

import org.junit.Before;
import org.yx.log.ConsoleLog;
import org.yx.log.LogLevel;
import org.yx.main.SumkServer;

/**
 * 外键测试
 * 
 * @author 游夏
 *
 */
public class BaseOrmTest {

	@Before
	public void before() {
		SumkServer.start(Arrays.asList("nosoa", "nohttp"));
		ConsoleLog.setDefaultLevel(LogLevel.TRACE);
	}

}
