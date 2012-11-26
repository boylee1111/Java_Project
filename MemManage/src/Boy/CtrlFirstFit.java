package Boy;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class CtrlFirstFit implements ActionListener, KeyListener {
	private List<MemBlock> blockList;
	
	private MemFrame memFrame = null;
	
	// blockSize����Ҫ���ڴ�   UsedSize��ʹ�õ��ڴ�  largestSize��������ڴ�
	private int memNum, blockSize, UsedSize, largestSize;

	public CtrlFirstFit(MemFrame memFrame) {
		this.memFrame = memFrame;
		memNum = 0;
		blockSize = 0;
		UsedSize = 0;
		largestSize = Constants.memSize;
		blockList = new LinkedList<MemBlock>();
		blockList.add(new MemBlock(memNum++, largestSize));
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO ��Ӱ�ťί�� first-fit
		Object event = e.getSource();
		if (event == memFrame.firstAllocButton) {
			System.out.println("first alloc button");
			// TODO �ж��ڴ��Ƿ��㹻
			blockSize = valueOfText();
			allocMem();
			System.out.println(valueOfText());
		}
		if (event == memFrame.firstFreeButton)
			System.out.println("first free button");
//		if (event == memFrame.firstDemoButton)
//			System.out.println("first demo button");
//		if (event == memFrame.firstPackButton)
//			System.out.println("first pack button");
//		if (event == memFrame.firstLogButton)
//			System.out.println("first log button");
	}

	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO ��ӻس��¼� first-fit
		Object event = e.getSource();
		int keyCode = e.getKeyCode();
		if (event == memFrame.firstAllocText && keyCode == KeyEvent.VK_ENTER) {
			System.out.println("first alloc text");
		}
		if (event == memFrame.firstFreeText && keyCode == KeyEvent.VK_ENTER) {
			System.out.println("first free text");
		}
	}

	public void keyTyped(KeyEvent e) {
		
	}
	
	public int valueOfText() {
		int value = 0;
		try {
			value = Integer.valueOf(memFrame.firstAllocText.getText().toString());
		} catch (NumberFormatException e) {
			// TODO ������������ֵ��쳣
			System.out.println("����������");
		}
		return value;
	}
	
	public boolean allocMem() {		
		if (blockSize > largestSize)
			return false;
		
		int i = 0;		
		for (Iterator<MemBlock> it = blockList.iterator(); it.hasNext(); i++) {
			MemBlock tmpBlock = (MemBlock)it.next();
			if (tmpBlock.size > blockSize && !tmpBlock.getUsed()) {
				
			}
		}
		
		return true;
	}
}