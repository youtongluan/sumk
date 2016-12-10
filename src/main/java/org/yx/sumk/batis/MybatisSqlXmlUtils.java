package org.yx.sumk.batis;

import java.io.InputStream;
import java.util.Map;

import org.yx.bean.Loader;
import org.yx.conf.AppInfo;
import org.yx.conf.MultiResourceFactory;
import org.yx.util.Assert;

/**
 * 需要[]来分段<BR>
 * 以#注释
 * 
 * @author 游夏
 *
 */
public final class MybatisSqlXmlUtils {

	public static Map<String, InputStream> openInputs(String db) throws Exception {
		String resourceFactory = AppInfo.get("sumk.db.batis.factory." + db, LocalSqlXmlFactory.class.getName());
		Class<?> factoryClz = Loader.loadClass(resourceFactory);
		Assert.isTrue(MultiResourceFactory.class.isAssignableFrom(factoryClz),
				resourceFactory + " should extend from MultiResourceFactory");
		MultiResourceFactory factory = (MultiResourceFactory) factoryClz.newInstance();
		return factory.openInputs(db);
	}

}
