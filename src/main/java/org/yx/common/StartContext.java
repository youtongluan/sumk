package org.yx.common;

import java.util.HashMap;
import java.util.Map;

public class StartContext {
	public static final String HTTP_LOGIN_PATH="http.login.path";
	public static StartContext inst = new StartContext();

	public ThreadLocal<Map<String,Object>> map=new ThreadLocal<Map<String,Object>>(){
		@Override
		protected Map<String,Object> initialValue() {
			return new HashMap<>();
		}
		
	};
	
	public static void clear() {
		inst = null;
	}
}
