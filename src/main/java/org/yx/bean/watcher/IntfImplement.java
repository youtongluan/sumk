package org.yx.bean.watcher;

import java.io.InputStream;
import java.util.Map;

import org.yx.bean.Bean;
import org.yx.bean.InnerIOC;
import org.yx.bean.Loader;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.util.CollectionUtils;
import org.yx.util.StringUtils;

@Bean
public class IntfImplement implements Scaned {

	@Override
	public void afterScaned() {
		try {
			InputStream in = Loader.getResourceAsStream("META-INF/sumk-intf");
			if (in == null) {
				Log.get("SYS").error("sumk-intf file cannot found");
				return;
			}
			Map<String, String> map = CollectionUtils.loadMap(in);
			for (String key : map.keySet()) {
				if (StringUtils.isEmpty(key)) {
					continue;
				}
				String impl = AppInfo.get("sumk.intf." + key);
				if (StringUtils.isEmpty(impl)) {
					impl = map.get(key);
				}
				if (StringUtils.isEmpty(impl)) {
					continue;
				}
				Class<?> intfClz = Loader.loadClass(key);
				Class<?> implClz = Loader.loadClass(impl);
				InnerIOC.putClassByInterface(intfClz, implClz);
			}
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}

	}

}
