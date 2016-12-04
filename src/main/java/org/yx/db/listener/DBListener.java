package org.yx.db.listener;

import org.yx.db.event.DBEvent;
import org.yx.listener.Listener;

public interface DBListener<T extends DBEvent> extends Listener<T> {

}
