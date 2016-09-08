package org.yx.http;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HttpHolder {

	private static Map<String,Class<?>> pojoMap=new ConcurrentHashMap<String,Class<?>>();
	
	private static Map<String,HttpInfo> actMap=new ConcurrentHashMap<String,HttpInfo>();
	
	
	/**
	 * 根据方法的全名，获取参数列表
	 * @param method  classFullName.method
	 * @return null表示方法不存在,或者参数为空
	 */
	public static Class<?> getArgType(String method){
		String m=getArgClassName(method);
		return pojoMap.get(m);
	}
	
	private static String getArgClassName(String method){
		int k=method.lastIndexOf(".");
		return method.substring(0,k)+"_"+method.substring(k+1);
	}
	/**
	 * 根据soaName获取MethodInfo
	 * @param method
	 * @return
	 */
	public static HttpInfo getHttpInfo(String name){
		return actMap.get(name);
	}

	public static void putActInfo(String name, HttpInfo actInfo) {
		actMap.putIfAbsent(name, actInfo);
	}
	
	public static Set<String> actSet(){
		return actMap.keySet();
	}
	/**
	 * 获取所有的http接口
	 * @return
	 */
	public static String[] acts(){
		return actMap.keySet().toArray(new String[0]);
	}
}
