package org.yx.db.conn;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.db.event.DBEvent;
import org.yx.db.event.DBEventPublisher;
import org.yx.db.event.ModifyEvent;

public class EventLane {
	private static ThreadLocal<Map<Connection, EventLane>> POOL = new ThreadLocal<Map<Connection, EventLane>>() {
		@Override
		protected Map<Connection, EventLane> initialValue() {
			return new HashMap<Connection, EventLane>();
		}

	};
	private List<DBEvent> events = new ArrayList<DBEvent>();

	public EventLane() {
	}

	private static EventLane pool(Connection conn) {
		return POOL.get().get(conn);
	}

	public static void pubuish(Connection conn, DBEvent event) {
		if (event == null) {
			return;
		}
		EventLane pool = pool(conn);
		if (pool == null) {
			pool = new EventLane();
			POOL.get().put(conn, pool);
		}
		if (ModifyEvent.class.isInstance(event)) {
			pool.events.add(event);
		} else {
			DBEventPublisher.publish(event);
		}
	}

	static void realPubuish(Connection conn) {
		EventLane pool = pool(conn);
		if (pool == null) {
			return;
		}
		for (DBEvent event : pool.events) {
			DBEventPublisher.publish(event);
		}
	}

	public static void remove(Connection conn) {
		POOL.get().remove(conn);
	}

	public static void removeALL() {
		POOL.remove();
	}
}
