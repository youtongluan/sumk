package org.yx.db.listen;

import org.yx.listener.DBEvent;
import org.yx.listener.Listener;

/**
 * 所有的事件，它都要监听
 * @see <code>ForAllListenerGroup</code>
 *
 */
public abstract class HearAllDBListener implements Listener<DBEvent> {

	@Override
	public boolean accept(DBEvent event) {
		return true;
	}

	@Override
	public String[] getTags() {
		return null;
	}
}