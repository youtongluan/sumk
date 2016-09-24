package org.yx.exception;

public class HttpException extends RuntimeException {

	private static final long serialVersionUID = 453453454L;
	private final Class<?> container;
	private final String msg;

	public HttpException(Class<?> container, String msg) {
		super(msg);
		this.container = container;
		this.msg = msg;
	}

	public HttpException(Class<?> container, String msg, Throwable exception) {
		super(msg, exception);
		this.container = container;
		this.msg = msg;
	}

	public static void throwException(Class<?> container, String msg) {
		throw new HttpException(container, msg);
	}

	public static void throwException(Class<?> container, String msg, Throwable exception) {
		throw new HttpException(container, msg, exception);
	}

	public Class<?> getContainer() {
		return container;
	}

	@Override
	public String getMessage() {
		return msg;
	}

}
