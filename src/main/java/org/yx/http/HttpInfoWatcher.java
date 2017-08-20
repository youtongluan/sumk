/**
 * Copyright (C) 2016 - 2017 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.http;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yx.bean.Bean;
import org.yx.bean.Plugin;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtil;
import org.yx.validate.Param;
import org.yx.validate.ParamInfo;

@Bean
public class HttpInfoWatcher implements Plugin {

	@Override
	public void start() {
		if (!AppInfo.getBoolean("sumk.http.print.actions", false)) {
			return;
		}
		Log.get("sumk.http").info("------\n{}\n", GsonUtil.gsonBuilder("httpinfo").create().toJson(infos()));

	}

	@Override
	public void stop() {

	}

	public static List<Map<String, Object>> infos() {
		List<Map<String, Object>> ret = new ArrayList<>(HttpHolder.actMap.size());
		HttpHolder.actMap.forEach((name, http) -> {
			Map<String, Object> map = new LinkedHashMap<>();
			ret.add(map);
			map.put("name", name);
			if (http.action != null) {
				Web web = http.action;
				map.put("requireLogin", web.requireLogin());
				map.put("requestEncrypt", web.requestEncrypt());
				map.put("responseEncrypt", web.responseEncrypt());
				map.put("sign", web.sign());
				map.put("description", web.description());
			}
			map.put("upload", http.upload != null);
			List<Map<String, Object>> list = new ArrayList<>();
			int paramSize = http.argNames == null ? 0 : http.argNames.length;
			for (int i = 0; i < paramSize; i++) {
				Map<String, Object> param = new LinkedHashMap<>();
				list.add(param);
				param.put("name", http.argNames[i]);
				param.put("type", http.argTypes[i].getName());
				ParamInfo pi = http.paramInfos == null ? null : http.paramInfos[i];
				if (pi != null) {
					Param p = pi.getParam();
					if (StringUtil.isNotEmpty(p.cnName())) {
						param.put("cnName", p.cnName());
					}
					param.put("required", p.required());
					if (p.length() > -1) {
						param.put("length", p.length());
					}
					if (p.maxLength() > -1) {
						param.put("maxLength", p.maxLength());
					}
					if (p.minLength() > -1) {
						param.put("minLength", p.minLength());
					}
				}
			}
			map.put("params", list);
			map.put("result", describe(http.method.getReturnType()));
		});
		return ret;
	}

	/**
	 * 获取一个类的字段描述。如果超类和子类存在同名字段，以超类为准
	 * 
	 * @param clazz
	 * @return
	 */
	private static Map<String, String> describe(Class<?> clazz) {
		Map<String, String> map = new HashMap<>();
		Class<?> tempClz = clazz;
		while (tempClz != null && !tempClz.getName().startsWith("java.")) {

			Field[] fs = tempClz.getDeclaredFields();
			for (Field f : fs) {
				map.putIfAbsent(f.getName(), f.getType().getSimpleName());
			}
			tempClz = tempClz.getSuperclass();
		}
		return map;
	}

}
