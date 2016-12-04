package org.yx.exception;

/**
 * 这个异常表示是执行sumk框架中的某个方法抛出的异常
 * 
 * @author 游夏
 *
 */
public class SumkException extends RuntimeException {

	private static final long serialVersionUID = 3453246435546L;
	private int code;

	public SumkException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public SumkException(int code, String msg, Throwable exception) {
		super(msg, exception);
		this.code = code;
	}

	public static void throwException(int code, String msg) throws SumkException {
		throw new SumkException(code, msg);
	}

	public static void throwException(int code, String msg, Throwable exception) throws SumkException {
		if (SumkException.class.isInstance(exception)) {
			throw (SumkException) exception;
		}
		throw new SumkException(code, msg, exception);
	}

	public static void throwException(String msg) throws SumkException {
		throw new SumkException(0, msg);
	}

	public static void throwException(String msg, Throwable exception) throws SumkException {
		if (SumkException.class.isInstance(exception)) {
			throw (SumkException) exception;
		}
		throw new SumkException(0, msg, exception);
	}

	public int getCode() {
		return code;
	}

	public static SumkException create(Throwable e) {
		if (SumkException.class.isInstance(e)) {
			throw (SumkException) e;
		}
		return new SumkException(0, e.getMessage(), e);
	}

}
