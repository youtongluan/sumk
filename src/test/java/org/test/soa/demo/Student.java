package org.test.soa.demo;

import java.util.Date;
import java.util.Map;

public class Student {
	private long id;
	private int age;
	private Date birthDay;
	private String name;
	private Map<String, Object> map;

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Student(Long id, int age, Date birthDay, String name, Map<String, Object> map) {
		this.id = id;
		this.age = age;
		this.birthDay = birthDay;
		this.name = name;
		this.map = map;
	}

	public Student() {
		super();
	}

}
