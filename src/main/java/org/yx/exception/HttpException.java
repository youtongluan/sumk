package org.yx.exception;

public class HttpException extends RuntimeException {

	private static final long serialVersionUID = 453453454L;
	private final Class<?> container;

	public HttpException(Class<?> container, String msg) {
		super(msg);
		this.container = container;
	}

	public HttpException(Class<?> container, String msg, Throwable exception) {
		super(msg, exception);
		this.container = container;
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

}
