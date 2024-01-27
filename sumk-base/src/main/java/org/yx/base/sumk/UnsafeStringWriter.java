package org.yx.base.sumk;

import java.io.IOException;
import java.io.Writer;

public class UnsafeStringWriter extends Writer {

	private final StringBuilder buf;

	public UnsafeStringWriter() {
		this(32);
	}

	public UnsafeStringWriter(int initialSize) {
		if (initialSize < 0) {
			throw new IllegalArgumentException("Negative buffer size");
		}
		buf = new StringBuilder(initialSize);
		lock = buf;
	}

	public UnsafeStringWriter(StringBuilder sb) {
		this.buf = sb;
		this.lock = buf;
	}

	public void write(int c) {
		buf.append((char) c);
	}

	public void write(char cbuf[], int off, int len) {
		if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		buf.append(cbuf, off, len);
	}

	public void write(String str) {
		buf.append(str);
	}

	public void write(String str, int off, int len) {
		buf.append(str.substring(off, off + len));
	}

	public UnsafeStringWriter append(CharSequence csq) {
		if (csq == null) {
			write("null");
		} else {
			buf.append(csq);
		}
		return this;
	}

	public UnsafeStringWriter append(CharSequence csq, int start, int end) {
		CharSequence cs = (csq == null ? "null" : csq);
		write(cs.subSequence(start, end).toString());
		return this;
	}

	public UnsafeStringWriter append(char c) {
		write(c);
		return this;
	}

	public String toString() {
		return buf.toString();
	}

	public StringBuilder getBuffer() {
		return buf;
	}

	public void flush() {
	}

	/**
	 * 空方法，close后不影响使用
	 */
	public void close() throws IOException {
	}

}
