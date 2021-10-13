package org.test.orm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yx.bean.IOC;
import org.yx.demo.member.Multikey;
import org.yx.orm.MultikeyDao;

public class MultiPrimaryTest extends BaseOrmTest{

	private MultikeyDao dao;

	@Before
	public void before() {
		dao = IOC.get(MultikeyDao.class);
	}

	@Test
	public void crud() {
		Multikey obj = dao.insert(null);
		Assert.assertEquals(obj, dao.query(obj.getId1(), obj.getId2()));
		dao.fullUpate(obj.getId1(), obj.getId2());
		Multikey real = new Multikey();
		real.setId1(obj.getId1()).setId2(obj.getId2());
		real.setName("全部更新，除名字外都清空");
		Assert.assertEquals(real, dao.query(obj.getId1(), obj.getId2()));

		Assert.assertEquals(1, dao.delete(real));
		Assert.assertNull(dao.query(obj.getId1(), obj.getId2()));
	}

}
