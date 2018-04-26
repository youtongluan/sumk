/**
 * Copyright (C) 2016 - 2030 youtongluan.
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
package org.yx.bean.watcher;

import java.lang.reflect.Field;

import org.yx.bean.Bean;
import org.yx.bean.Loader;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.util.secury.AESEncry;
import org.yx.util.secury.Encry;
import org.yx.util.secury.EncryUtil;
import org.yx.util.secury.FastEncry;

@Bean
public class EncryBuilder implements Scaned {

	@Override
	public void afterScaned() {
		String cipher = AppInfo.get("sumk.encry.cipher");
		if (cipher == null || cipher.length() == 0) {
			return;
		}
		try {
			Encry en = null;
			if ("fast".equalsIgnoreCase(cipher)) {
				en = new FastEncry();
			} else if ("AES".equalsIgnoreCase(cipher)) {
				en = new AESEncry();
			} else {
				Class<?> clz = Loader.loadClass(cipher);
				en = (Encry) clz.newInstance();
			}
			Field f = EncryUtil.class.getDeclaredField("encry");
			f.setAccessible(true);
			f.set(null, en);
			Log.get("sumk.SYS").trace("encry:{}", en);
		} catch (Exception e1) {
			Log.printStack(e1);
		}

	}

}
