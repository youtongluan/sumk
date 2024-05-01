/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.log.impl;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import org.yx.base.matcher.BooleanMatcher;
import org.yx.conf.AppInfo;
import org.yx.log.ConsoleLog;
import org.yx.log.LogSettings;
import org.yx.util.StringUtil;
import org.yx.util.SumkDate;

public abstract class RollingFileAppender extends LogQueue implements LogAppender {

	public static final String SLOT = "#";
	private static final long DAY_MILS = 1000L * 3600 * 24;

	protected StringBuilder buffer;
	protected String currentDate;
	protected FileChannel channel;

	private boolean dirty;
	private int bufferSize = AppInfo.getInt("sumk.log.buffer.size", 2048);

	protected int syncInterval = AppInfo.getInt("sumk.log.sync.interval", 3000);

	private long lastDeleteTime;

	private long lastSyncTime;

	protected String filePattern;
	protected File dir;

	protected boolean setupFilePath(String fileName) {
		if (StringUtil.isEmpty(fileName)) {
			return false;
		}
		if (fileName.indexOf(SLOT) < 0) {
			ConsoleLog.defaultLog.error("{} should contain {}", fileName, SLOT);
			return false;
		}
		if (fileName.indexOf(SLOT) != fileName.lastIndexOf(SLOT)) {
			ConsoleLog.defaultLog.error("{} contain more than one {}", fileName, SLOT);
			return false;
		}
		File file = new File(fileName);
		this.filePattern = file.getName();
		if (!this.filePattern.contains(SLOT)) {
			ConsoleLog.defaultLog.error("{} should contain {}", this.filePattern, SLOT);
			return false;
		}
		this.dir = file.getParentFile();
		if (!this.dir.exists() && !this.dir.mkdirs()) {
			ConsoleLog.defaultLog.error("directory [{}{}] is not exists, and cannot create!!!",
					this.dir.getAbsolutePath(), File.pathSeparator);
			return false;
		}

		return true;
	}

	public RollingFileAppender(String name) {
		super(name);
	}

	@Override
	protected void flush(boolean idle) {

		if (this.buffer != null && this.buffer.length() > bufferSize) {
			this.buffer = null;
		}
		if (this.getMatcher() == BooleanMatcher.FALSE && this.channel != null) {
			this.sync();
			this.closeCurrentChannel();
		}
		long now = System.currentTimeMillis();
		if (this.dirty && (now - this.lastSyncTime >= this.syncInterval)) {
			this.lastSyncTime = now;
			this.sync();
		}

		if (now - this.lastDeleteTime >= DAY_MILS) {
			this.lastDeleteTime = now;
			this.deleteHisLog();
		}
	}

	protected void deleteHisLog() {
		String[] files = dir.list();
		if (files == null || files.length == 0) {
			return;
		}
		for (String f : files) {
			if (this.shouldDelete(f)) {
				try {
					File log = new File(dir, f);
					log.delete();
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	protected void output(List<LogObject> msgs) throws IOException {
		String date = this.formatDateString(msgs.get(0).logDate);
		int fromIndex = 0;
		for (int i = 1; i < msgs.size(); i++) {
			LogObject obj = msgs.get(i);
			String d2 = this.formatDateString(obj.logDate);
			if (!d2.equals(date)) {
				this.outputInSameDate(date, msgs.subList(fromIndex, i));
				date = d2;
				fromIndex = i;
			}
		}
		if (fromIndex == 0) {
			this.outputInSameDate(date, msgs);
			return;
		}

		this.outputInSameDate(date, msgs.subList(fromIndex, msgs.size()));
	}

	protected void outputInSameDate(String date, List<LogObject> msgs) throws IOException {
		if (!date.equals(this.currentDate)) {
			if (this.channel != null) {
				this.sync();
				this.closeCurrentChannel();
			}
			File file = new File(this.dir, filePattern.replace(SLOT, date));
			if (!file.exists() && !file.createNewFile()) {
				ConsoleLog.defaultLog.error("{} create fail ", file.getAbsolutePath());
				for (LogObject logObject : msgs) {
					System.err.print(LogHelper.plainMessage(logObject, LogSettings.showAttach()));
				}
				return;
			}
			this.channel = FileChannel.open(file.toPath(), StandardOpenOption.APPEND);
			this.currentDate = date;
		}

		long size = 0;
		ByteBuffer[] bufs = new ByteBuffer[msgs.size()];
		for (int i = 0; i < bufs.length; i++) {
			byte[] b = toBytes(msgs.get(i));
			bufs[i] = ByteBuffer.wrap(b);
			size += b.length;
		}

		do {
			size -= this.channel.write(bufs);
		} while (size != 0);
		this.dirty = true;
	}

	protected void sync() {
		try {
			this.channel.force(true);
			this.dirty = false;
			ConsoleLog.defaultLog.trace("{} finish sync to {}", this.name, this.currentDate);
		} catch (Exception e) {
			ConsoleLog.defaultLog.error(this.name + "刷新到磁盘失败[" + this.currentDate + "]", e);
		}
	}

	protected void closeCurrentChannel() {
		try {
			this.channel.close();
			this.channel = null;
			this.currentDate = null;
			ConsoleLog.defaultLog.debug("{} closed {}", this.name, this.currentDate);
		} catch (Exception e) {
			ConsoleLog.defaultLog.error(this.name + "关闭失败[" + this.currentDate + "]", e);
		}
	}

	protected abstract boolean shouldDelete(String fileName);

	protected byte[] toBytes(LogObject logObject) {
		if (this.buffer == null) {
			this.buffer = new StringBuilder(bufferSize);
		} else {
			this.buffer.setLength(0);
		}
		LogHelper.plainMessage(this.buffer, logObject, LogSettings.showAttach());
		return this.buffer.toString().getBytes(LogObject.CHARSET);
	}

	protected abstract String formatDateString(SumkDate date);

	protected String extractPath(Map<String, String> map) {
		String path = map.get(LogAppenders.PATH);
		if (path != null || map.size() != 1) {
			return path;
		}

		String p = map.keySet().iterator().next();
		String v = map.get(p);
		return StringUtil.isEmpty(v) ? p : String.join(":", p, v);
	}

	@Override
	protected boolean onStart(Map<String, String> map) {
		this.config(map);
		return this.dir != null && this.filePattern != null;
	}

	@Override
	public void config(Map<String, String> map) {
		String path = extractPath(map);
		if (!setupFilePath(path)) {
			return;
		}
		super.config(map);
	}

	public File getLogFile(SumkDate date) {
		String name = filePattern.replace(SLOT, formatDateString(date));
		return new File(dir, name);
	}

	public void setSyncInterval(int syncInterval) {
		this.syncInterval = syncInterval;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = Math.max(bufferSize, 100);
	}

}
