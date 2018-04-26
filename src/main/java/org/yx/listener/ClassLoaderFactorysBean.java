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
package org.yx.listener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yx.bean.BeanWrapper;
import org.yx.bean.Loader;
import org.yx.bean.watcher.BeanWatcher;
import org.yx.common.Ordered;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public abstract class ClassLoaderFactorysBean<T extends Ordered> implements FactorysBean<T>, BeanWatcher<T> {

	protected String userPackage;

	protected String sumkPath;

	protected String sumkPackage_pre;

	protected List<T> beans;

	protected Set<T> userBeans = new HashSet<>();

	protected Comparator<Ordered> comparator;

	public ClassLoaderFactorysBean(String userPackage, String sumkPath, String sumkPackage_pre) {
		this.userPackage = userPackage;
		this.sumkPath = sumkPath == null ? "" : sumkPath.replace('.', '/');
		if (StringUtil.isNotEmpty(sumkPackage_pre)) {
			this.sumkPackage_pre = sumkPackage_pre.endsWith(".") ? sumkPackage_pre : sumkPackage_pre + ".";
		}
	}

	@SuppressWarnings("unchecked")
	protected void createSumkObject() throws Exception {
		if (StringUtil.isEmpty(sumkPath)) {
			return;
		}
		InputStream in = Loader.getResourceAsStream("META-INF/" + sumkPath);
		if (in == null) {
			Log.get("sumk.SYS").error(sumkPath + " file cannot found");
			return;
		}
		List<String> sumks = CollectionUtil.loadList(in);
		for (String listener : sumks) {
			if (StringUtil.isNotEmpty(sumkPackage_pre)) {
				listener = AppInfo.get("sumk.class.load." + listener, sumkPackage_pre + listener);
			}
			Class<?> clz = this.getClass().getClassLoader().loadClass(listener);
			if (!acceptClass().isAssignableFrom(clz)) {
				Log.get("sumk.ClassLoaderFactorysBean").debug("{} is not a Listener", listener);
				continue;
			}
			beans.add((T) clz.newInstance());
		}
	}

	@Override
	public List<T> create() throws Exception {
		beans = new ArrayList<>();
		createSumkObject();
		this.beans.addAll(this.userBeans);

		Collections.sort(beans, comparator);
		return beans;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void beanPost(BeanWrapper w) {
		Object obj = w.getBean();
		if (userPackage != null && acceptClass().isInstance(obj)
				&& w.getTargetClass().getName().startsWith(this.userPackage)) {
			this.userBeans.add((T) obj);
		}
	}

}
