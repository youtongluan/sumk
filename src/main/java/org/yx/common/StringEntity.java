package org.yx.common;

public class StringEntity {
	private String name;
	private String value;

	public StringEntity(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return name + "=" + value;
	}

}
