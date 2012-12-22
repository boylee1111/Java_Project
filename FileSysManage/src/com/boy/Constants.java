package com.boy;

enum FCB_Type { directory, file };
enum Status_Type { all_right, dupilication_of_name, illegal_name, memory_lack };

public class Constants {
	// UI��������
	static int FRAME_LENGTH = 800, FRAME_WIDTH = 600;
	static int BUTTON_PANE_LENGTH = FRAME_LENGTH, BUTTON_PANE_WIDTH = 100;
	static int TREE_AND_EDIT_PANE_LENGTH = FRAME_LENGTH, TREE_AND_EDIT_PANE_WIDTH = FRAME_WIDTH - BUTTON_PANE_WIDTH;
	static int NUMBER_OF_BUTTONS = 8;

	// �ļ�ϵͳ����������
	static int BLOCK_SIZE = 256; // ÿһ����Ĵ�С
	static int CLUSTER_SIZE = BLOCK_SIZE * 4; // ÿһ���صĴ�С
	static int CLUSTER_NUM = 2048;
	static int MEMORY_SIZE = CLUSTER_SIZE * CLUSTER_NUM; // �ڴ��С
	
	static int END_OF_FAT = -1; // Fat�������־
	static int PARENT_OF_ROOT = -1; // �����ĸ��ڵ�
	
//	static char END_OF_FILE = '#'; // �ļ�������־
}
