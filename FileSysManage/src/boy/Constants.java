package boy;

enum File_Type { directory, file };

public class Constants {
	static int BLOCK_SIZE = 256; // ÿһ����Ĵ�С
	static int CLUSTER_SIZE = BLOCK_SIZE * 4; // ÿһ���صĴ�С
	static int CLUSTER_NUM = 2048;
	static int MEMORY_SIZE = CLUSTER_SIZE * CLUSTER_NUM; // �ڴ��С
	
//	static char END_OF_FILE = '#'; // �ļ�������־
}
