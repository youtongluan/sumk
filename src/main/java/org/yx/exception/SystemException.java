package org.yx.exception;

public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 3453246435546L;
	private int code;
	private String msg;

	public SystemException(int code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

	public SystemException(int code, String msg, Throwable exception) {
		super(msg, exception);
		this.code = code;
		this.msg = msg;
	}

	public static void throwException(int code, String msg) {
		throw new SystemException(code, msg);
	}

	public static void throwException(int code, String msg, Throwable exception) {
		throw new SystemException(code, msg, exception);
	}

	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return msg;
	}

}
