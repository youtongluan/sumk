package org.yx.rpc.server;

import org.yx.common.Ordered;

public interface RequestHandler extends Ordered {
	boolean accept(Object message);

	Object received(Object message);
}
