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
import java.util.ArrayList;
import java.util.List;

import org.yx.conf.AppInfo;
import org.yx.util.UUIDSeed;

public class LocalFileDao implements UnionLogDao {
	private static final String LINE_SPLIT = "\n";
	private final long MAX_FILE_LENGTH = AppInfo.getInt("sumk.log.union.max_file_length", 100 * 1024 * 1024);
	private final int MAX_RECORD_SIZE = AppInfo.getInt("sumk.log.union.max_record_size", 200);
	private int aliveTime = AppInfo.getInt("sumk.log.union.alive_time", 15000);

	private long createTime = -1;

	private int fileLength;
	private int recordSize;
	private List<byte[]> buffer;

	public LocalFileDao() {
		this.buffer = new ArrayList<>(MAX_RECORD_SIZE);
	}

	private File createLogingFile() {
		for (int i = 0; i < 5; i++) {
			try {
				String name = AppInfo.pid().concat("_").concat(UUIDSeed.seq18());
				File f = new File(getLogingPath(), name);
				if (!f.getParentFile().exists()) {
					File parent = f.getParentFile();
					if (!parent.mkdirs()) {
						LogAppenders.consoleLog.error("create folder " + parent.getAbsolutePath() + " failed!!!");
						return null;
					}
				}
				if (!f.createNewFile()) {
					LogAppenders.consoleLog.error("create file " + f.getAbsolutePath() + " failed!!!");
					return null;
				}
				return f;
			} catch (Exception e) {
				LogAppenders.consoleLog.error(e.getMessage(), e);
			}
		}
		return null;
	}

	@Override
	public void store(List<UnionLogObject> logs) throws IOException {
		int logCount = logs.size();

		StringBuilder sb = new StringBuilder(600 * Math.min(10, logs.size()));
		for (UnionLogObject log : logs) {
			sb.append(log.log).append(LINE_SPLIT);
		}
		byte[] bs = sb.toString().getBytes(AppInfo.UTF8);
		sb = null;
		buffer.add(bs);
		this.recordSize += logCount;
		this.fileLength += bs.length;
		if (this.recordSize >= MAX_RECORD_SIZE || this.fileLength > MAX_FILE_LENGTH) {
			reset();
		}
	}

	@Override
	public void flush(boolean idle) {
		if (this.recordSize >= MAX_RECORD_SIZE || this.fileLength > MAX_FILE_LENGTH
				|| System.currentTimeMillis() - createTime >= aliveTime) {
			reset();
		}
	}

	public void reset() {
		List<byte[]> datas = this.buffer;
		if (datas.isEmpty()) {
			return;
		}
		this.buffer = new ArrayList<>();
		long size = 0;
		FileChannel channel = null;
		try {
			File logFile = createLogingFile();
			channel = FileChannel.open(logFile.toPath(), StandardOpenOption.APPEND);
			ByteBuffer[] bufs = new ByteBuffer[datas.size()];
			for (int i = 0; i < bufs.length; i++) {
				bufs[i] = ByteBuffer.wrap(datas.get(i));
				size += datas.get(i).length;
			}

			do {
				size -= channel.write(bufs);
			} while (size != 0);
			channel.force(true);
			channel.close();
			channel = null;
			move2Loged(logFile);
			this.fileLength = 0;
			this.recordSize = 0;
		} catch (Exception e) {
			LogAppenders.consoleLog.error(e.toString(), e);
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					LogAppenders.consoleLog.error(e.toString(), e);
				}
			}
		}
	}

	protected void move2Loged(File logFile) {
		UnionLogUtil.move2Loged(logFile);
	}

	protected File getLogingPath() {
		return UnionLogUtil.getLogingPath();
	}

	public void setAliveTime(int time) {
		this.aliveTime = time;
	}
}
