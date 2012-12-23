package com.boy;

import java.io.*;
import java.util.*;

public class FileFCB implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fileName = null;
	private int ID; // ʹ��hashCode��ΪID
	private int parentID;
	private long createDate; // �����ļ�ʱ�䣬ʱ���
	private long modifyDate; // ����޸��ļ�ʱ�䣬ʱ���
	// TODO ACL���ò���
	private Map<String, Set<String>> accessList = null; // ACL
	private FCB_Type FCBType;
	private	int fileSize;
	private FileFat fileFat = null;
	
	public FileFCB(String fileName) {
		this.fileName = fileName;
		ID = this.hashCode();
		createDate = modifyDate = System.currentTimeMillis();
		// TODO ��ʼ��ACL
//		accessList = new HashMap<String, Set<String>>();
//		accessList.put("read", new HashSet<String>());
//		accessList.put("write", new HashSet<String>());
		fileSize = 0;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getParentID() {
		return parentID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(long modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Map<String, Set<String>> getAccessList() {
		return accessList;
	}

	public void setAccessList(Map<String, Set<String>> accessList) {
		this.accessList = accessList;
	}

	public FCB_Type getFCBType() {
		return FCBType;
	}

	public void setFCBType(FCB_Type fCBType) {
		FCBType = fCBType;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public FileFat getFileFat() {
		return fileFat;
	}

	public void setFileFat(FileFat fileFat) {
		this.fileFat = fileFat;
	}
}
