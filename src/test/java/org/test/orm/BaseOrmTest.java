package org.test.orm;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.yx.log.LogLevel;
import org.yx.log.Loggers;
import org.yx.main.SumkServer;

/**
 * 外键测试
 * 
 * @author 游夏
 *
 */
public class BaseOrmTest {

	@BeforeClass
	public static void beforeClass() {
		SumkServer.start(Arrays.asList("nosoa", "nohttp"));
		Loggers.setDefaultLevel(LogLevel.DEBUG);
	}

}
