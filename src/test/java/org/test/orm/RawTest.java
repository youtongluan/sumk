package org.test.orm;

import org.junit.Test;
import org.yx.bean.IOC;
import org.yx.orm.RawDao;

public class RawTest extends BaseOrmTest {

	@Test
	public void test() {
		IOC.get(RawDao.class).test();
	}

}
