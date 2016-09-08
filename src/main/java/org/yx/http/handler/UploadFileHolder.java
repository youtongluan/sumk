package org.yx.http.handler;

import java.util.List;

public class UploadFileHolder {
	private static ThreadLocal<List<UploadFile>> files=new ThreadLocal<>();
	
	public static List<UploadFile> getFiles() {
		return files.get();
	}

	static void setFiles(List<UploadFile> fs) {
		files.set(fs);
	}
	
	static void remove(){
		files.remove();
	}
	
}
