package boy;

public class FileFAT {
	private int ID; // ��ʾ����hashCode��ʾ
	private int nextID; // ��һ����ʾ����hashCode��ʾ
	private int clusterNum;
	private boolean isUsed;
	
	public FileFAT() {
		ID = this.hashCode();
		isUsed = false;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getNextID() {
		return nextID;
	}

	public void setNextID(int nextID) {
		this.nextID = nextID;
	}

	public int getClusterNum() {
		return clusterNum;
	}

	public void setClusterNum(int clusterNum) {
		this.clusterNum = clusterNum;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
}
