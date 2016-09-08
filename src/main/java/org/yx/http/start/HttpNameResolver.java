package org.yx.http.start;

import java.lang.reflect.Method;

import org.springframework.util.StringUtils;

class HttpNameResolver {
	
	public String solve(Class<?> clz,Method m,String name){
		if(name!=null){
			name=name.trim(); 
			if(name.length()>0){
				return name;
			}
		}
		return StringUtils.uncapitalize(clz.getSimpleName())+"."+m.getName();
	}
}
