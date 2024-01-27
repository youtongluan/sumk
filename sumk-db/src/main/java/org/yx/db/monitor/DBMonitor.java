package org.yx.db.monitor;

import static org.yx.common.monitor.Monitors.BLANK;
import static org.yx.conf.AppInfo.LN;

import java.util.List;

import org.yx.common.monitor.MessageProvider;
import org.yx.conf.AppInfo;
import org.yx.db.conn.DataSourceManager;
import org.yx.db.conn.DataSources;
import org.yx.db.mapper.NamedExecutor;
import org.yx.db.sql.PojoMeta;
import org.yx.db.sql.PojoMetaHolder;
import org.yx.db.sql.VisitCounter;
import org.yx.db.visit.SumkStatement;
import org.yx.util.StringUtil;

public class DBMonitor implements MessageProvider {
	public String dbVisitInfo() {
		List<PojoMeta> list = PojoMetaHolder.allPojoMeta();
		StringBuilder sb = new StringBuilder(128);
		sb.append("##sql总执行次数").append(BLANK).append(SumkStatement.getExecuteCount()).append(BLANK).append(BLANK)
				.append("SDB执行次数").append(BLANK).append(NamedExecutor.getExecuteCount()).append(LN);
		sb.append("#tableName").append(BLANK).append("queryCount").append(BLANK).append("modifyCount").append(BLANK)
				.append("cacheKeyVisits").append(BLANK).append("cacheKeyHits").append(AppInfo.LN);
		long modify, query, cacheVisit, cacheHit;
		long totalModify = 0, totalQuery = 0, totalCacheVisit = 0, totalCacheHit = 0;
		for (PojoMeta p : list) {
			VisitCounter c = p.getCounter();
			query = c.getQueryCount();
			modify = c.getModifyCount();
			sb.append(p.getTableName()).append(BLANK).append(query).append(BLANK).append(modify).append(BLANK);
			totalModify += modify;
			totalQuery += query;
			if (p.isNoCache()) {
				sb.append("-").append(BLANK).append("-").append(AppInfo.LN);
				continue;
			}
			cacheVisit = c.getCacheKeyVisits();
			cacheHit = c.getCacheKeyHits();
			sb.append(cacheVisit).append(BLANK).append(cacheHit).append(AppInfo.LN);
			totalCacheVisit += cacheVisit;
			totalCacheHit += cacheHit;
		}
		sb.append("*total*").append(BLANK).append(totalQuery).append(BLANK).append(totalModify).append(BLANK)
				.append(totalCacheVisit).append(BLANK).append(totalCacheHit).append(AppInfo.LN);
		return sb.toString();
	}

	public String dataSourceStatus(String name) {
		if (StringUtil.isEmpty(name)) {
			return null;
		}
		StringBuilder sb = new StringBuilder(200);
		if (!DataSources.getManagerSelector().dbNames().contains(name)) {
			sb.append(DataSources.getManagerSelector().dbNames());
			return sb.toString();
		}
		DataSourceManager manager = DataSources.getManager(name);
		if (manager == null) {
			return sb.toString();
		}
		sb.append(manager.status());
		return sb.toString();
	}

	@Override
	public Object get(String type, String key, Object param) {
		if ("monitor".equals(type)) {
			if ("db.cache".equals(key)) {
				return this.dbVisitInfo();
			}
			if ("datasource".equals(key)) {
				return this.dataSourceStatus((String) param);
			}
		}

		return null;
	}
}
