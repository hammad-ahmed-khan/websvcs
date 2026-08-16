package com.extra.oms.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * LockService.java
 * aibrahim
 * 2024
 */
@Service
public class LockService {

	@Value("${lock.filename}")
	private String fileName;

	private FileChannel globalFileChannel = null;

	private FileLock globalFileLock = null;

	public void lockApplication() throws Exception {
		File file = new File(fileName);
		@SuppressWarnings("resource")
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
		globalFileChannel = randomAccessFile.getChannel();
		globalFileLock = globalFileChannel.tryLock();
		if (globalFileLock == null) {
			throw new RuntimeException("application already running");
		}
	}

	public void unlockApplication() {
		try {
			globalFileLock.release();
			globalFileLock = null;
		} catch (Exception e) { }
		try {
			globalFileChannel.close();
			globalFileChannel = null;
		} catch (Exception e) { }
	}
}
