package com.boy;

import java.io.*;

public class FileFat implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int ID; // ��ʾ����hashCode��ʾ
	private int nextID; // ��һ����ʾ����hashCode��ʾ
	private String data = null;
	private int usedSize;
	private boolean isUsed;
	
	public FileFat() {
		ID = this.hashCode();
		nextID = -1;
		isUsed = false;
		usedSize = 0;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getNextID() {
		return nextID;
	}

	public void setNextID(int nextID) {
		this.nextID = nextID;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public int getUsedSize() {
		return usedSize;
	}

	public void setUsedSize(int usedSize) {
		this.usedSize = usedSize;
	}
}