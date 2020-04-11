package org.yx.common;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class UnsafeByteArrayOutputStream extends OutputStream {

	private byte buf[];

	private int count;

	public UnsafeByteArrayOutputStream(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		buf = new byte[size];
	}

	private void ensureCapacity(int minCapacity) {
		if (minCapacity - buf.length > 0)
			grow(minCapacity);
	}

	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	private void grow(int minCapacity) {
		int oldCapacity = buf.length;
		int newCapacity = oldCapacity << 1;
		if (newCapacity - minCapacity < 0)
			newCapacity = minCapacity;
		if (newCapacity - MAX_ARRAY_SIZE > 0)
			newCapacity = hugeCapacity(minCapacity);
		buf = Arrays.copyOf(buf, newCapacity);
	}

	private static int hugeCapacity(int minCapacity) {
		if (minCapacity < 0)
			throw new OutOfMemoryError();
		return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
	}

	public void write(int b) {
		ensureCapacity(count + 1);
		buf[count] = (byte) b;
		count += 1;
	}

	public void write(byte b[], int off, int len) {
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) - b.length > 0)) {
			throw new IndexOutOfBoundsException();
		}
		ensureCapacity(count + len);
		System.arraycopy(b, off, buf, count, len);
		count += len;
	}

	public void writeTo(OutputStream out) throws IOException {
		out.write(buf, 0, count);
	}

	public void reset() {
		count = 0;
	}

	public byte[] toByteArray() {
		return Arrays.copyOf(buf, count);
	}

	public int size() {
		return count;
	}

	public String toString() {
		return new String(buf, 0, count);
	}

	public String toString(String charsetName) throws UnsupportedEncodingException {
		return new String(buf, 0, count, charsetName);
	}

	public byte[] extractRawData() {

		if (this.count == 0) {
			return new byte[0];
		}
		byte[] bs = this.buf;
		if (this.count != bs.length) {
			bs = Arrays.copyOf(bs, this.count);
		}
		return extractData(bs);
	}

	public static byte[] extractData(byte[] bs) {

		if (bs != null && bs.length > 4 && bs[0] == 100 && bs[1] == 97 && bs[2] == 116 && bs[3] == 97 && bs[4] == 61) {
			byte[] temp = new byte[bs.length - 5];
			System.arraycopy(bs, 5, temp, 0, temp.length);
			return temp;
		}
		return bs;
	}
}
