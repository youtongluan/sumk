package org.yx.listener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yx.bean.BeanWrapper;
import org.yx.bean.watcher.BeanWatcher;
import org.yx.common.Ordered;
import org.yx.log.Log;
import org.yx.main.Bootstrap;
import org.yx.util.CollectionUtils;
import org.yx.util.StringUtils;

public abstract class ClassLoaderFactorysBean<T extends Ordered> implements FactorysBean<T>, BeanWatcher<T> {

	protected String userPackage;

	protected String sumkPath;

	protected String sumkPackage_pre;

	protected List<T> beans;

	protected Set<T> userBeans = new HashSet<>();

	protected Comparator<Ordered> comparator;

	/**
	 * 
	 * @param userListenerPackage
	 *            外部自定义的包路径
	 * @param sumkListenerPath
	 *            内部listener文件的相对路径（相对于ClassLoader)
	 * @param sumkListenerPackage_pre
	 *            部listener的前缀。如这个值是org.yx，那么org.yx.
	 *            BeanFactory只要写成BeanFactory就可以了
	 */
	public ClassLoaderFactorysBean(String userPackage, String sumkPath, String sumkPackage_pre) {
		this.userPackage = userPackage;
		this.sumkPath = sumkPath == null ? "" : sumkPath.replace('.', '/');
		if (StringUtils.isNotEmpty(sumkPackage_pre)) {
			this.sumkPackage_pre = sumkPackage_pre.endsWith(".") ? sumkPackage_pre : sumkPackage_pre + ".";
		}
	}

	@SuppressWarnings("unchecked")
	protected void createSumkObject() throws Exception {
		if (StringUtils.isEmpty(sumkPath)) {
			return;
		}
		InputStream in = Bootstrap.class.getClassLoader().getResourceAsStream("META-INF/" + sumkPath);
		if (in == null) {
			Log.get("SYS").error(sumkPath + " file cannot found");
			return;
		}
		List<String> sumks = CollectionUtils.loadList(in);
		for (String listener : sumks) {
			if (StringUtils.isNotEmpty(sumkPackage_pre)) {
				listener = sumkPackage_pre + listener;
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
