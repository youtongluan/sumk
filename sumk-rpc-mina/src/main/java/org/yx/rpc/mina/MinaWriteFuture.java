package org.yx.rpc.mina;

import java.util.Objects;

import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.yx.rpc.transport.RpcWriteFuture;
import org.yx.rpc.transport.RpcWriteListener;

public class MinaWriteFuture implements RpcWriteFuture {
	private final WriteFuture future;

	public MinaWriteFuture(WriteFuture future) {
		this.future = Objects.requireNonNull(future);
	}

	@Override
	public boolean isWritten() {
		return future.isWritten();
	}

	@Override
	public Throwable getException() {
		return future.getException();
	}

	@Override
	public void addListener(RpcWriteListener listener) {
		future.addListener(new MinaWriteListener(Objects.requireNonNull(listener), this));
	}

	private static final class MinaWriteListener implements IoFutureListener<WriteFuture> {
		private final RpcWriteListener listener;
		private final MinaWriteFuture writeFuture;

		public MinaWriteListener(RpcWriteListener listener, MinaWriteFuture writeFuture) {
			this.listener = listener;
			this.writeFuture = writeFuture;
		}

		@Override
		public void operationComplete(WriteFuture future) {
			listener.afterWrited(writeFuture);
		}

	}
}
