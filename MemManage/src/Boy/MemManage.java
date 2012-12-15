package boy;

// MemManage.java -- Main

public class MemManage {
	public static void main(String[] args) {
		SwingUtil.setLookAndFeel();
		MemFrame backFrame = new MemFrame();
		CtrlFirstFit firstFit = new CtrlFirstFit(backFrame);
		CtrlBestFit bestFit = new CtrlBestFit(backFrame);
		backFrame.initWithAlgo(firstFit, bestFit);
	}
}