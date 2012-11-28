package Boy;

import java.util.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import Boy.Constants.Type;

public class CtrlFirstFit implements ActionListener, KeyListener {
	private LinkedList<MemBlock> blockList;
	
	private MemFrame memFrame = null;
	
	MemLogCat logCat = null;
	
	Constants.Type type = Type.FIRST_FIT;
	
	// blockSize����Ҫ���ڴ�   UsedSize��ʹ�õ��ڴ�  largestSize��������ڴ�
	private int memNum, largestSize;
	private float usedSize;
	
	Scanner scanner = null;

	public CtrlFirstFit(MemFrame memFrame) {
		this.memFrame = memFrame;
		memNum = 0;
		usedSize = 0;
		largestSize = Constants.memSize;
		blockList = new LinkedList<MemBlock>();
		logCat = new MemLogCat();
		logCat.setTitle("First-Fit LogCat");
	}

	public void actionPerformed(ActionEvent e) {
		initList();
		Object event = e.getSource();
		if (event == memFrame.firstAllocButton) {
			int blockSize = Constants.valueOfText(memFrame.firstAllocText);
			if (blockSize != -1) {
				String memName = "Job#" + memNum;
				if (!allocMem(memName,  blockSize)) {
					logCat.appendLog("Allocation unsuccessfully!");
				}
			}
		}
		if (event == memFrame.firstFreeButton) {
			int jobNum = Constants.valueOfText(memFrame.firstFreeText);
			if (jobNum != -1){
				String memName = "Job#" + jobNum;
				freeMem(memName);
			}
		}
		if (event == memFrame.firstPackButton) {
			if (!Constants.pack(memFrame, blockList, type)) {
				JOptionPane.showMessageDialog(null, 
						"Please allocte memory first",
						"Error",
						JOptionPane.WARNING_MESSAGE);
			} else {
				largestSize = Constants.getLargestSize(blockList);
				System.out.println(largestSize);
			}
		}
		if (event == memFrame.firstDemoButton) {
			try {
				scanner = new Scanner(new File(".\\Resource\\Task.txt"));
				scanner.useDelimiter(System.getProperty("line.separator"));
				Demo();
			} catch (FileNotFoundException fileE) {
				JOptionPane.showMessageDialog(null, 
						"File can't find, please check",
						"Error",
						JOptionPane.WARNING_MESSAGE);
			}
		}
		if (event == memFrame.firstClearButton) {
//			Constants.clear(memFrame, blockList, type);
//			largestSize = Constants.memSize;
//			usedSize = 0;
//			memNum = 0;
//			logCat.setRate(0.0f);
		}
		if (event == memFrame.firstLogButton) {
			logCat.setVisible(true);
		}
	}

	public void keyReleased(KeyEvent e) {
		initList();
		Object event = e.getSource();
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER && event == memFrame.firstAllocText) {
			int blockSize = Constants.valueOfText(memFrame.firstAllocText);
			if (blockSize != -1) {
				String memName = "Job#" + memNum;
				if (!allocMem(memName,  blockSize)) {
					logCat.appendLog("Allocate unsuccessfully!");
				}
			}
		}
		if (keyCode == KeyEvent.VK_ENTER && event == memFrame.firstFreeText) {
			int jobNum = Constants.valueOfText(memFrame.firstFreeText);
			String memName = "Job#" + jobNum;
			freeMem(memName);
		}
	}
	
	public boolean allocMem(String name, int size) {
		if (size > largestSize) {
			JOptionPane.showMessageDialog(null, 
					"Memory shortage, please try packing then allocate.",
					"Error",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (size < Constants.memLeast) {
			JOptionPane.showMessageDialog(null, 
					"Please allocate 10K at least.",
					"Error",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		int i = 0;
		MemBlock newBlock = null;
		for (Iterator<MemBlock> it = blockList.iterator(); it.hasNext(); i++) {
			MemBlock tmpBlock = (MemBlock)it.next();
			if (tmpBlock.size >= size && !tmpBlock.getUsed()) {
				int restSize = tmpBlock.size - size;
				tmpBlock.name = name;
				tmpBlock.size = size;
				tmpBlock.setUsed(true);
				usedSize += size;

				if (restSize != 0){
					tmpBlock.setBounds(2, tmpBlock.beginY, Constants.blockWidth, tmpBlock.size / Constants.factor);
					newBlock = new MemBlock("free", restSize);
					newBlock.beginY = tmpBlock.beginY + tmpBlock.size / Constants.factor;
					newBlock.setBounds(2, newBlock.beginY, Constants.blockWidth, newBlock.size / Constants.factor);
					newBlock.setUsed(false);
					blockList.add(i + 1, newBlock);
					memFrame.firstMemPane.add(newBlock);
				}
				break;
			}
		}
		
		// Get the largest size that hasn't used
		largestSize = Constants.getLargestSize(blockList);
		logCat.appendLog("Allocate " + size + "K successfully!");
		logCat.setRate(usedSize / Constants.memSize);

		memNum++;
		return true;
	}
	
	public void freeMem(String name) {
		MemBlock freeBlock = null;
		for (Iterator<MemBlock> it = blockList.iterator(); it.hasNext();) {
			MemBlock tmpBlock = (MemBlock)it.next();
			if (tmpBlock.name.equals(name) && tmpBlock.getUsed())
				freeBlock = tmpBlock;
		}
		
		if (freeBlock != null) {
			int i = 0;
			freeBlock.setUsed(false);
			usedSize -= freeBlock.size;
			MemBlock preBlock = null, nextBlock = null;
			
			i = blockList.indexOf(freeBlock);
			// i < blockList.size(), has next
			if (i < blockList.size() - 1) {
				nextBlock = blockList.get(i + 1);
				if (!nextBlock.getUsed()){
					freeBlock.size += nextBlock.size;
					freeBlock.setBounds(2, freeBlock.beginY, Constants.blockWidth, freeBlock.size / Constants.factor);
					blockList.remove(nextBlock);
					memFrame.firstMemPane.remove(nextBlock);
				}
			}
			
			i = blockList.indexOf(freeBlock);
			// i > 0, has previous
			if (i > 0) {
				preBlock = blockList.get(i - 1);
				if (!preBlock.getUsed()) {
					preBlock.size += freeBlock.size;
					preBlock.setBounds(2, preBlock.beginY, Constants.blockWidth, preBlock.size / Constants.factor );
					blockList.remove(freeBlock);
					memFrame.firstMemPane.remove(freeBlock);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, 
					"NO " + name + "!",
					"Error",
					JOptionPane.WARNING_MESSAGE);
			logCat.appendLog("Free unsuccessfully!");
			return;
		}
		
		// Get the largest size that hasn't used
		largestSize = Constants.getLargestSize(blockList);
		logCat.appendLog("Free " + name + " successfully!");
		logCat.setRate(usedSize / Constants.memSize);
	}

	// TODO�½�һ����
	private void Demo() {
		Thread firstDemo = new Thread(new Runnable() {
			public void run() {
				while (scanner.hasNext()) {
					Scanner lineScanner = new Scanner(scanner.next());
					String name = lineScanner.next();
					boolean allocOrFree = lineScanner.nextBoolean();
					int size = lineScanner.nextInt();
					
					if (allocOrFree) {
						if (!allocMem(name,  size)) {
							logCat.appendLog("Allocation unsuccessfully!");
						}
					} else {
						freeMem(name);
					}

					try {
						Thread.currentThread();
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				scanner.close();
			}
		});
		firstDemo.start();
	}
	  
	// Initialize list
	public void initList() {
		if (blockList.isEmpty()) {
			MemBlock firstBlock = new MemBlock("free", largestSize);
			firstBlock.setBounds(2, 2, Constants.blockWidth, Constants.memSize / Constants.factor);
			blockList.add(firstBlock);
			memFrame.firstMemPane.add(firstBlock);
		}
	}
	
	public void keyPressed(KeyEvent e) {
		
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
}