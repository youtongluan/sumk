package org.yx.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class SoaException extends RuntimeException {

	private static final long serialVersionUID = 453453343454L;
	private int code;
	private String detailError;
	private String exceptionClz;

	
	public String getDetailError() {
		return detailError;
	}

	public String getExceptionClz() {
		return exceptionClz;
	}

	public SoaException(int code, String msg, Throwable e) {
		super(msg);
		this.code = code;
		this.detailError=getException(e);
		this.exceptionClz=e==null?"":e.getClass().getName();
	}

	public static void throwException(int code, String msg, Throwable exception) {
		if(msg==null||msg.length()==0){
			msg=exception.getMessage();
		}
		throw new SoaException(code, msg, exception);
	}

	public static void throwException(Throwable exception) {
		throw new SoaException(-1, exception.getMessage(), exception);
	}

	public int getCode() {
		return code;
	}

	private static String getException(Throwable e) {
		if(e==null){
			return "Exception is NULL";
		}
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		e.printStackTrace(w);
		if (sw.toString().length() >= 4000) {
			return sw.toString().substring(0, 4000);
		}
		return sw.toString();
	}

}
