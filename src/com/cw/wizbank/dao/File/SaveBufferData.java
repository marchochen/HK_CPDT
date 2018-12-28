package com.cw.wizbank.dao.File;

public class SaveBufferData {
	public final static int bufferSize = 5 * 1024 * 1024;
	private byte[] savaData = null;
	private int length = 0;

	public SaveBufferData() {
	}

	public SaveBufferData(byte[] savadata, int length) {
		this.savaData = savadata;
		this.length = length;
	}

	public byte[] getSavaData() {
		return savaData;
	}

	public void setSavaData(byte[] savaData) {
		this.savaData = savaData;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
