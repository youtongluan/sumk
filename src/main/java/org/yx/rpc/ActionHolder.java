package org.yx.rpc;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ActionHolder {
	
	private static Map<String,Class<?>> pojoMap=new ConcurrentHashMap<String,Class<?>>();
	
	private static Map<String,ActionInfo> actMap=new ConcurrentHashMap<String,ActionInfo>();
	
	
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
	public static ActionInfo getActionInfo(String soaName){
		return actMap.get(soaName);
	}

	public static void putActInfo(String soaName, ActionInfo actInfo) {
		actMap.putIfAbsent(soaName, actInfo);
	}
	
	public static Set<String> soaSet(){
		return actMap.keySet();
	}
}
