package org.test.orm;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.test.inner.dao.DemoUserDao;
import org.test.inner.po.DemoUser;
import org.yx.bean.IOC;
import org.yx.main.SumkServer;
import org.yx.util.IOUtil;

//单主键的测试
public class SinglePrimaryTest extends BaseOrmTest{

	private DemoUserDao dao;

	@Before
	public void before() {
		dao = IOC.get(DemoUserDao.class);
	}

	@Test
	public void crud() {
		DemoUser obj = new DemoUser();
		obj.setAge(new Random().nextInt(100));
		obj.setName("名字" + new Random().nextInt());
		obj.setLastUpdate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));// timestamp类型的字段，如果是mysql数据库。用Timestamp或Date类都行，但oracle只能用Timestamp类型
		dao.insert(obj);
		Assert.assertEquals(obj, dao.query(obj.getId()));
		dao.fullUpate(obj.getId());
		DemoUser real = new DemoUser();
		real.setId(obj.getId());
		real.setName("全部更新，除名字外都清空");
		Assert.assertEquals(real, dao.query(obj.getId()));

		dao.softDelete(obj.getId());
		Assert.assertNull(dao.query(obj.getId()));
	}

	@Test
	public void select() throws ParseException {
		dao.select();
	}

	@Test
	public void lockFile() throws Exception {
		InputStream in=SumkServer.class.getClassLoader().getResourceAsStream("META-INF/lua_del");
		byte[] bs=IOUtil.readAllBytes(in, true);
		Assert.assertEquals(95, bs.length);
		String script=new String(bs);
		Assert.assertFalse(script.contains("\r"));
	}
}
