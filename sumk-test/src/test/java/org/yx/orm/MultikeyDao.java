package org.yx.orm;

import java.util.Random;

import org.yx.annotation.Bean;
import org.yx.annotation.db.Box;
import org.yx.db.DB;
import org.yx.demo.member.Multikey;
import org.yx.util.UUIDSeed;

@Bean
public class MultikeyDao {
	Random r = new Random();

	@Box
	public Multikey insert(Multikey obj) {
		if (obj == null) {
			obj = create("名字" + r.nextInt(), r.nextInt(100));
		}
		System.out.println("插入：" + DB.insert(obj).execute() + "条，id1=" + obj.getId1());
		return obj;
	}

	@Box
	public void updatePart(Multikey obj) {
		obj.setName("名字改为：" + r.nextInt());
		DB.update(obj).execute();
	}

	@Box
	public void fullUpate(String id1, String id2) {
		Multikey obj = new Multikey();
		obj.setId1(id1).setId2(id2);
		obj.setName("全部更新，除名字外都清空");
		DB.update(obj).fullUpdate(true).execute();
	}

	@Box
	public int delete(Multikey obj) {
		return DB.delete(obj).execute();
	}

	@Box
	public Multikey query(String id1, String id2) {
		return DB.select(new Multikey().setId1(id1).setId2(id2)).queryOne();
	}

	private Multikey create(String name, int age) {
		Multikey obj = new Multikey();
		obj.setId1(UUIDSeed.seq());
		obj.setId2(UUIDSeed.seq());
		obj.setAge(age);
		obj.setName(name);
		return obj;
	}

	@Box
	public void incrAge(Multikey obj, int age) {
		DB.update(obj).incrNum("age", age).execute();
	}

}
