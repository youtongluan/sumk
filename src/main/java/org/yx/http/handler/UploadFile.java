package org.yx.http.handler;

import java.io.InputStream;

public class UploadFile {
	String name;
	String fieldName;
	long size;
	InputStream inputStream;

	public String getName() {
		return name;
	}

	public String getFieldName() {
		return fieldName;
	}

	public long getSize() {
		return size;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

}
