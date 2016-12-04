package org.yx.exception;

public class BizException extends RuntimeException {

	private static final long serialVersionUID = 453453454L;

	private final int code;

	public BizException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public BizException(int code, String msg, Throwable exception) {
		super(msg, exception);
		this.code = code;
	}

	public static void throwException(int code, String msg) throws BizException {
		throw new BizException(code, msg);
	}

	public static void throwException(int code, String msg, Throwable exception) throws BizException {
		throw new BizException(code, msg, exception);
	}

	public int getCode() {
		return code;
	}

}
