package org.yx.asm;

import java.lang.reflect.Field;

import org.yx.util.UUIDSeed;

public class GIDHolder {
	private static ThreadLocal<GIDObject> tl = new ThreadLocal<GIDObject>() {
		@Override
		protected GIDObject initialValue() {
			return new GIDObject("#" + UUIDSeed.seq(), false);
		}

	};

	public static void handle(String sid) {
		if (sid != null && sid.length() > 0) {
			sid = sid.trim();
			if (sid.isEmpty() || sid.contains("/") || sid.contains("\\") || sid.contains(".")) {
				return;
			}
			if (sid.startsWith("#")) {
				tl.set(new GIDObject(sid, true));
			} else {
				tl.set(new GIDObject("#" + sid, true));
			}
		}
	}

	public static void clear() {
		tl.remove();
	}

	public static String gid() {
		GIDObject gid = tl.get();
		if (gid.manClose) {
			return gid.gid;
		}
		try {
			Long c = getCount();
			if (c == null) {
				if (gid.first) {
					gid.first = false;
					return gid.gid;
				} else {
					gid.gid = newRelativeGid(gid.gid);
					return gid.gid;
				}
			}
			if (gid.first) {
				gid.first = false;
				gid.count = c.longValue();
				return gid.gid;
			}
			if (gid.count == c.longValue()) {
				return gid.gid;
			}
			gid.count = c.longValue();
			gid.gid = newRelativeGid(gid.gid);
			return gid.gid;
		} catch (Exception e) {
			gid.gid = newRelativeGid(gid.gid);
			return gid.gid;
		}

	}

	static String newRelativeGid(String old) {
		if (old.contains("-")) {
			String g = old.substring(old.indexOf("-") + 1);
			return "#" + g + "-" + UUIDSeed.seq();
		} else {
			return old + "-" + UUIDSeed.seq();
		}
	}

	public static String get() {
		GIDObject gid = tl.get();
		if (gid.manClose) {
			return gid.gid;
		}
		try {
			Long c = getCount();
			if (c == null) {
				return gid.gid;
			}
			if (gid.count == c.longValue()) {
				return gid.gid;
			}
			if (gid.count == -1) {
				gid.count = c.longValue();
				return gid.gid;
			}
			gid.count = c.longValue();
			gid.gid = newRelativeGid(gid.gid);
			return gid.gid;
		} catch (Exception e) {
			gid.gid = newRelativeGid(gid.gid);
			return gid.gid;
		}
	}

	private static class GIDObject {
		public GIDObject(String id, boolean b) {
			this.gid = id;
			this.manClose = b;
		}

		String gid;
		boolean manClose;
		boolean first = true;
		long count = -1;

	}

	private static Field threadF, workF;
	static {

		try {
			threadF = Thread.class.getDeclaredField("target");
			threadF.setAccessible(true);
			workF = Class.forName("java.util.concurrent.ThreadPoolExecutor$Worker").getDeclaredField("completedTasks");
			workF.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Long getCount() throws IllegalArgumentException, IllegalAccessException {
		if (threadF == null || workF == null) {
			return null;
		}
		Object obj = threadF.get(Thread.currentThread());
		if (obj == null || !obj.getClass().getName().startsWith("java.util.concurrent.ThreadPoolExecutor")) {
			return null;
		}
		Object v = workF.get(obj);
		if (!Long.class.isInstance(v)) {
			return null;
		}
		return (Long) v;

	}

}
