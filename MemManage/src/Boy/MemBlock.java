package Boy;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class MemBlock extends JLabel {
	private static final long serialVersionUID = 1L;

	private boolean isUsed;
	
	int beginY;
	
	String name;
	
	int size;
	
	public MemBlock(String name, int size) {
		this.name = name;
		this.size = size;
		beginY = 2;
		isUsed = false;
		this.setOpaque(true);
		this.setBackground(Color.GREEN);
		this.setBorder(new EtchedBorder(EtchedBorder.RAISED));
	}
	
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
		if (this.isUsed) {
			this.setBackground(Color.RED);
			this.setText(name + "# " + size + 'K');
		} else {
			this.setBackground(Color.GREEN);
			this.setText("");
			name = "free";
		}
	}
	
	public boolean getUsed() {
		return isUsed;
	}
}