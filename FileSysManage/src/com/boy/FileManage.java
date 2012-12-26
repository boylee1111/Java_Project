package com.boy;

import java.io.Serializable;
import java.util.*;
import java.util.regex.*;

public class FileManage implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<FileFCB> FCBList = null;
	private List<FileFat> FatList = null;
	
	public FileManage() {
		FCBList = new LinkedList<FileFCB>();
		FatList = new LinkedList<FileFat>();
		FileFCB rootFCB = new FileFCB("Root");
		rootFCB.setParentID(Constants.PARENT_OF_ROOT); // ��Ŀ¼�ĸ��ڵ�ID�涨Ϊ0
		rootFCB.setFCBType(FCB_Type.directory);
		FCBList.add(rootFCB);
	}

	public List<FileFCB> getFCBList() {
		return FCBList;
	}

	public void setFCBList(List<FileFCB> FCBList) {
		this.FCBList = FCBList;
	}

	public List<FileFat> getFatList() {
		return FatList;
	}

	public void setFatList(List<FileFat> FatList) {
		this.FatList = FatList;
	}
	
	public FileFCB getRoot() {
		return FCBList.get(0);
	}

	public void formatFileSystem() {
		FileFCB rootFCB = FCBList.get(0);
		FCBList.clear();
		FatList.clear();
		rootFCB.setParentID(Constants.PARENT_OF_ROOT); // ��Ŀ¼�ĸ��ڵ�ID�涨Ϊ0
		rootFCB.setFCBType(FCB_Type.directory);
		FCBList.add(rootFCB);
		this.fresh();
	}

	public Status_Type createDir(String dirName, int parentID, FileFCB dirFCB) {
		if (this.isDupilicationOfName(dirName, parentID, FCB_Type.directory))
			return Status_Type.dupilication_of_name;
		if (this.isNameLegal(dirName))
			return Status_Type.illegal_name;
		dirFCB.setParentID(parentID);
		dirFCB.setFCBType(FCB_Type.directory);
		FCBList.add(dirFCB);
		return Status_Type.all_right;
	}
	
	public Status_Type createFile(String fileName, int parentID, FileFCB fileFCB) {
		if (this.isDupilicationOfName(fileName, parentID, FCB_Type.file))
			return Status_Type.dupilication_of_name;
		if (this.isNameLegal(fileName))
			return Status_Type.illegal_name;
		fileFCB.setParentID(parentID);
		fileFCB.setFCBType(FCB_Type.file);
		FileFat fileFat = new FileFat();
		fileFat.setData("");
		fileFat.setUsed(true);
		fileFat.setNextID(Constants.END_OF_FAT);
		fileFCB.setFileFat(fileFat);
		FatList.add(fileFat);
		FCBList.add(fileFCB);
		return Status_Type.all_right;
	}
	
	public void deleteDir(int dirID) {
		List<FileFCB> toDeleteList = new LinkedList<FileFCB>();
		this.searchFCBByParentID(dirID, toDeleteList);
		int deleteSize = toDeleteList.size();
		for (int i = 0; i < deleteSize; i++) {
			FileFCB tmpFCB = toDeleteList.get(i);
			if (tmpFCB.getFCBType() == FCB_Type.directory)
				this.recursiveDeleteDir(tmpFCB.getID(), toDeleteList);
		}
		// ����ɾ��������Ҫɾ�����ļ�
		for (Iterator<FileFCB> it = toDeleteList.iterator(); it.hasNext();) {
			FileFCB tmpFCB = (FileFCB)it.next();
			if (tmpFCB.getFCBType() == FCB_Type.file)
				this.deleteFile(tmpFCB.getID());
		}
		FCBList.removeAll(toDeleteList);
		FCBList.remove(this.searchFCBByID(dirID));
	}
	
	public void deleteFile(int fileID) {
		// ����ļ�FCB��Fat������
		FileFCB fileFCB = this.searchFCBByID(fileID);
		FileFat fileFat = fileFCB.getFileFat();
		int nextID;
		FCBList.remove(fileFCB);
		do {
			fileFat.setUsed(false);
			nextID = fileFat.getNextID();
			fileFat = this.searchNextFatByID(nextID);
		} while (nextID != Constants.END_OF_FAT);
		fileFat.setUsed(false);
	}
	
	public Status_Type rename(int fileID, String newName) {
		if (this.isNameLegal(newName)) {
			return Status_Type.illegal_name;
		}
		FileFCB changedFCB = this.searchFCBByID(fileID);
		int parentID = changedFCB.getParentID();
		List<FileFCB> siblingList = new LinkedList<FileFCB>();
		this.searchFCBByParentID(parentID, siblingList);
		for (Iterator<FileFCB> it = FCBList.iterator(); it.hasNext();) {
			FileFCB tmpFCB = it.next();
			if (tmpFCB.getFileName().equals(newName)) {
				return Status_Type.dupilication_of_name;
			}
		}
		changedFCB.setModifyDate(System.currentTimeMillis());
		changedFCB.setFileName(newName);
		return Status_Type.all_right;
	}
	
	public Status_Type move(int fileID, int parentID) {
		FileFCB movedFCB = this.searchFCBByID(fileID);
		List<FileFCB> targetSubFileList = new LinkedList<FileFCB>();
		this.searchFCBByParentID(parentID, targetSubFileList);
		for (Iterator<FileFCB> it = targetSubFileList.iterator(); it.hasNext();) {
			FileFCB tmpFCB = (FileFCB)it.next();
			if (tmpFCB.getFileName().equals(movedFCB.getFileName())) {
				return Status_Type.dupilication_of_name;
			}
		}
		movedFCB.setParentID(parentID);
		for (int i = 0; i < 50; i++)
			this.fresh();
		return Status_Type.all_right;
	}
	
	public Status_Type saveFile(int fileID, String buffer) {
		FileFCB fileFCB = this.searchFCBByID(fileID);
		FileFCB parentFCB = this.searchFCBByID(fileFCB.getParentID());
		FileFat fileFat = fileFCB.getFileFat();
		if (this.getRoot().getFileSize() - fileFCB.getFileSize() + buffer.length() > Constants.MEMORY_SIZE)
			return Status_Type.memory_lack;
		String[] subString = this.splitString(buffer);
		if (subString.length == 1 || subString.length == 0)
		{
			fileFat.setData(buffer);
			fileFat.setUsedSize(buffer.length());
			fileFat.setNextID(Constants.END_OF_FAT);
		} else if (buffer.length() <= Constants.CLUSTER_SIZE) { 
			fileFat.setData(buffer);
			fileFat.setUsedSize(buffer.length());
			fileFat.setNextID(Constants.END_OF_FAT);			
		} else {
			FileFat[] tmpFats = new FileFat[subString.length - 1];
			for (int i = 0; i < tmpFats.length; i++) {
				tmpFats[i] = new FileFat();
				tmpFats[i].setUsed(true);
				tmpFats[i].setData(subString[i]);
				tmpFats[i].setUsedSize(subString[i].length());
				FatList.add(tmpFats[i]);
			}
			fileFat.setNextID(tmpFats[0].getID());
			for (int i = 0; i < tmpFats.length - 1; i++) {
				tmpFats[i].setNextID(tmpFats[i + 1].getID());
			}
			tmpFats[tmpFats.length - 1].setNextID(Constants.END_OF_FAT);
		}
		fileFCB.setFileSize(buffer.length());
		fileFCB.setModifyDate(System.currentTimeMillis());
		parentFCB.setModifyDate(System.currentTimeMillis());
		for (int i = 0; i < 50; i++)
			this.fresh();
		this.getRoot().setFileSize(this.getRootSize());
		return Status_Type.all_right;
	}
	
	public int getRootSize() {
		FileFCB rootFCB = this.getRoot();
		int totalSize = 0;
		for (Iterator<FileFCB> it = FCBList.iterator(); it.hasNext();) {
			FileFCB tmpFCB = (FileFCB)it.next();
			if (tmpFCB.getParentID() == rootFCB.getID()) {
				totalSize += tmpFCB.getFileSize();
			}
		}
		return totalSize;
	}
	
	public String readFile(int fileID) {
		FileFCB fileFCB = this.searchFCBByID(fileID);
		FileFat fileFat = fileFCB.getFileFat();
		FileFat tmpFat = fileFat;
		String buffer = new String("");
		while (tmpFat.getNextID() != Constants.END_OF_FAT) {
			buffer = String.format(buffer + tmpFat.getData());
			tmpFat = this.searchFatByID(tmpFat.getNextID());
		}
		buffer = String.format(buffer + tmpFat.getData());
		return buffer;
	}
	
	public int getSubNumber(int dirID) {
		List<FileFCB> SubFileList = new LinkedList<FileFCB>();
		this.searchFCBByParentID(dirID, SubFileList);
		return SubFileList.size();
	}
	
	public void fresh() {
		for (Iterator<FileFCB> it = FCBList.iterator(); it.hasNext();) {
			FileFCB tmpFCB = (FileFCB)it.next();
			if (tmpFCB.getFCBType() == FCB_Type.directory) {
				int newSize = this.getDirSize(tmpFCB.getID());
				tmpFCB.setFileSize(newSize);
			}
		}
	}
	
	private int getDirSize(int dirID) {
		int totalSize = 0;
		for (Iterator<FileFCB> it = FCBList.iterator(); it.hasNext();) {
			FileFCB tmpFCB = (FileFCB)it.next();
			if (tmpFCB.getParentID() == dirID)
				totalSize += tmpFCB.getFileSize();
		}
		return totalSize;
	}
	
	private String[] splitString(String buffer) {
		int number = buffer.length() / Constants.CLUSTER_SIZE;
		int surplus = buffer.length() % Constants.CLUSTER_SIZE;
		if (surplus != 0)
			number++;
		String[] subString = new String[number];
		for (int i = 0; i < number; i++) {
			if (i == number - 1)
				subString[i] = buffer.substring(i * Constants.CLUSTER_SIZE);
			else
				subString[i] = buffer.substring(i * Constants.CLUSTER_SIZE, (i + 1) * Constants.CLUSTER_SIZE);
		}
		return subString;
	}
	
	// �ж������Ƿ�Ϸ�
	private boolean isNameLegal(String fileName) {
		if (fileName == null || fileName.length() <= 0 || fileName.length() >= 255)
			return false;
		String regex = "^[a-zA-Z_]+[a-zA-Z0-9_]*";
		return !Pattern.compile(regex).matcher(fileName).matches();
	}

	// ����Ƿ�����
	private boolean isDupilicationOfName(String fileName, int parentID, FCB_Type fileType) {
		for (Iterator<FileFCB> it = FCBList.iterator(); it.hasNext();) {
			FileFCB tmpFCB = (FileFCB)it.next();
			if (tmpFCB.getFileName().endsWith(fileName) && 
					tmpFCB.getParentID() == parentID &&
					tmpFCB.getFCBType() == fileType)
				return true;
		}
		return false;
	}

	// һЩ��FCB��Fat����ص���������
	private FileFCB searchFCBByID(int ID) {
		FileFCB tmpFCB = null;
		for (Iterator<FileFCB> it = FCBList.iterator(); it.hasNext();) {
			tmpFCB = (FileFCB)it.next();
			if (tmpFCB.getID() == ID)
				break;
		}
		return tmpFCB;
	}

	private void searchFCBByParentID(int parentID, List<FileFCB> subFCBList) {		
		FileFCB tmpFCB = null;
		for (Iterator<FileFCB> it = FCBList.iterator(); it.hasNext();) {
			tmpFCB = (FileFCB)it.next();
			if (tmpFCB.getParentID() == parentID) {
				subFCBList.add(tmpFCB);
			}
		}
	}

	private FileFat searchNextFatByID(int ID) {
		FileFat tmpFat = null;
		for (Iterator<FileFat> it = FatList.iterator(); it.hasNext();) {
			tmpFat = (FileFat)it.next();
			if (tmpFat.getID() == ID)
				break;
		}
		return tmpFat;
	}

	private FileFat searchFatByID(int ID) {
		FileFat tmpFat = null;
		for (Iterator<FileFat> it = FatList.iterator(); it.hasNext();) {
			tmpFat = (FileFat)it.next();
			if (tmpFat.getID() == ID)
				break;
		}
		return tmpFat;
	}

	private void recursiveDeleteDir(int dirID, List<FileFCB> toDeleteList) {  
		int beforeSize = toDeleteList.size();
		this.searchFCBByParentID(dirID, toDeleteList);
		int afterSize = toDeleteList.size();
		if (beforeSize == afterSize) {
			return ;
		}
		for (int i = beforeSize; i < afterSize; i++) {
			FileFCB tmpFCB = toDeleteList.get(i);
			if (tmpFCB.getFCBType() == FCB_Type.directory) {
				this.recursiveDeleteDir(tmpFCB.getID(), toDeleteList);
			}
		}
	}
}