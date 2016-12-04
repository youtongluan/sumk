package org.yx.db.event;

import java.util.List;
import java.util.Map;

/**
 * 查询事件。无论是in还是result，都排除了已经从redis中获取的部分
 * 
 * @author 游夏
 *
 */
public class QueryEvent extends DBEvent {
	public QueryEvent(String table) {
		super(table);
	}

	List<Map<String, Object>> in;
	List<?> result;

	public List<Map<String, Object>> getIn() {
		return in;
	}

	public void setIn(List<Map<String, Object>> in) {
		this.in = in;
	}

	public List<?> getResult() {
		return result;
	}

	public void setResult(List<?> result) {
		this.result = result;
	}

}
