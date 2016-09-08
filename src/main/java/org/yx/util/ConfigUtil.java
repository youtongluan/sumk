package org.yx.util;

import java.io.File;
import java.nio.file.Path;

public class ConfigUtil {

	public static interface FileMonitor{
		void handle(Path path,Kind kind);
	}
	
	public static enum Kind{
		CREATE,MODIFY,DELETE
	}
	
	public String getValue(String key){
		return System.getProperty(key);
	}
	public void init(File dir){
		
	}
	
}
