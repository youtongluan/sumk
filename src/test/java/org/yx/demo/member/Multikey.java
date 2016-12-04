package org.yx.demo.member;

import org.yx.db.annotation.Column;
import org.yx.db.annotation.Table;
import org.yx.db.dao.ColumnType;
import org.yx.db.dao.Pojo;

@Table
public class Multikey extends Pojo {

	@Column(columnType = ColumnType.ID_BOTH, columnOrder = 1)
	private String id1;
	@Column(columnType = ColumnType.ID_BOTH, columnOrder = 2)
	private String id2;
	private String name;
	private Integer age;

	public String getId1() {
		return id1;
	}

	public Multikey setId1(String id1) {
		this.id1 = id1;
		return this;
	}

	public String getId2() {
		return id2;
	}

	public Multikey setId2(String id2) {
		this.id2 = id2;
		return this;
	}

	public String getName() {
		return name;
	}

	public Multikey setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getAge() {
		return age;
	}

	public Multikey setAge(Integer age) {
		this.age = age;
		return this;
	}

	@Override
	public String toString() {
		return "Multikey [id1=" + id1 + ", id2=" + id2 + ", name=" + name + ", age=" + age + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((id1 == null) ? 0 : id1.hashCode());
		result = prime * result + ((id2 == null) ? 0 : id2.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Multikey other = (Multikey) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (id1 == null) {
			if (other.id1 != null)
				return false;
		} else if (!id1.equals(other.id1))
			return false;
		if (id2 == null) {
			if (other.id2 != null)
				return false;
		} else if (!id2.equals(other.id2))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}