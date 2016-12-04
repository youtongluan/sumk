package org.yx.bean.watcher;

import java.io.InputStream;
import java.util.List;

import org.yx.bean.Bean;
import org.yx.bean.Loader;
import org.yx.log.Log;
import org.yx.util.CollectionUtils;
import org.yx.util.StringUtils;

@Bean
public class ExtendRunner implements ContextWatcher {

	protected String userListenerPackage;

	@Override
	public void contextInitialized() {
		try {
			InputStream in = Loader.getResourceAsStream("META-INF/sumk-exec");
			if (in == null) {
				Log.get("SYS").error("sumk-exec file cannot found");
				return;
			}
			List<String> list = CollectionUtils.loadList(in);
			for (String key : list) {
				if (StringUtils.isEmpty(key)) {
					continue;
				}
				Class<?> clz = Loader.loadClass(key);
				if (!Runnable.class.isAssignableFrom(clz)) {
					Log.get("SYS").info("{} should implements Runnable", clz.getSimpleName());
					continue;
				}
				Runnable r = (Runnable) clz.newInstance();
				r.run();
			}
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}

	}

}
