package Boy;
// MemFrame.java -- Initialize the frame
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class MemFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	CtrlFirstFit firstFit = null;
	CtrlBestFit bestFit = null;

	// MenuBar��Menu and MenuItem
	private JMenuBar menuBar;
	private JMenu fileMenu, aboutMenu;
	private JMenuItem exitItem, aboutItem;
	
	private JPanel firstPane, bestPane; // Use GridLayout to layout
	private JPanel cutoffPane; // Cut different algorithm
	private JPanel firstMemPane, bestMemPane; // Memory Block Panel
	private JPanel contentPane; // Global Panel
	
	private Font font, titleFont, demoFont; // Different font
	
	// Constants Label
	private JLabel firstLabel, bestLabel;
	private JLabel firstKLabel, bestKLabel;
	private JLabel firstJobLabel, bestJobLabel;
	
	// Buttons, use as their name
	private JButton firstDemoButton, bestDemoButton;
	private JButton firstPackButton, bestPackButton;
	private JButton firstLogButton, bestLogButton;
	private JButton firstAllocButton, bestAllocButton;
	private JButton firstFreeButton, bestFreeButton;
	
	// Interaction
	JTextField firstAllocText, bestAllocText;
	JTextField firstFreeText, bestFreeText;

	public MemFrame() {
		super("Memory Management");
		this.setSize(800, 600);
	}
	
	public void initWithAlgo(CtrlFirstFit firstFitDel, CtrlBestFit bestFitDel) {
		firstFit = firstFitDel;
		bestFit = bestFitDel;
		
		// Create menu bar, menus and menu items
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File(F)");
		aboutMenu = new JMenu("About(A)");
		exitItem = new JMenuItem("Exit(E)", 'E');
		aboutItem = new JMenuItem("About(A)", 'A');

		// set shortcuts
		fileMenu.setMnemonic('F');
		aboutMenu.setMnemonic('A');
		exitItem.setMnemonic('E');
		aboutItem.setMnemonic('A');
		
		// Add
		exitItem.addActionListener(this);
		aboutItem.addActionListener(this);
		fileMenu.add(exitItem);
		aboutMenu.add(aboutItem);
		menuBar.add(fileMenu);
		menuBar.add(aboutMenu);
		this.setJMenuBar(menuBar);
		
		font = new Font("Times New Roman", Font.BOLD, 13);
		titleFont = new Font("Times New Roman", Font.BOLD, 25);
		demoFont = new Font("Times New Roman", Font.BOLD, 18);
		
		// Title
		firstLabel = new JLabel("First-Fit Algorithm", JLabel.CENTER);
		bestLabel = new JLabel("Best-Fit Algorithm", JLabel.CENTER);
		firstLabel.setForeground(Color.RED);
		bestLabel.setForeground(Color.RED);
		firstLabel.setFont(titleFont);
		bestLabel.setFont(titleFont);
		firstLabel.setBounds(50, 10, 300, 50);
		bestLabel.setBounds(450, 10, 300, 50);
		this.add(firstLabel);
		this.add(bestLabel);

		firstDemoButton = new JButton("First-Fit Demo");
		bestDemoButton = new JButton("Best-Fit Demo");
		firstDemoButton.setFont(demoFont);
		bestDemoButton.setFont(demoFont);
		firstDemoButton.setBounds(125, 390, 150, 50);
		bestDemoButton.setBounds(525, 390, 150, 50);
		this.add(firstDemoButton);
		this.add(bestDemoButton);
		
		firstPackButton = new JButton("First-Fit Pack");
		bestPackButton = new JButton("Best-Fit Pack");
		firstPackButton.setFont(font);
		bestPackButton.setFont(font);
		firstPackButton.setBounds(20, 450, 125, 40);
		bestPackButton.setBounds(420, 450, 125, 40);
		this.add(firstPackButton);
		this.add(bestPackButton);
		
		firstLogButton = new JButton("First-Fit LogCat");
		bestLogButton = new JButton("Best-Fit LogCat");
		firstLogButton.setFont(font);
		bestLogButton.setFont(font);
		firstLogButton.setBounds(20, 490, 125, 40);
		bestLogButton.setBounds(420, 490, 125, 40);
		this.add(firstLogButton);
		this.add(bestLogButton);
		
		firstPane = new JPanel();
		bestPane = new JPanel();
		cutoffPane = new JPanel();
		
		// Set the layout
		firstPane.setLayout(new GridLayout(2, 3, 5, 5));
		firstPane.setBounds(160, 450, 200, 80);
//		firstPane.setBackground(Color.WHITE);
		bestPane.setLayout(new GridLayout(2, 3, 5, 5));
		bestPane.setBounds(560, 450, 200, 80);
//		bestPane.setBackground(Color.WHITE);
		this.add(firstPane);
		this.add(bestPane);
		
		cutoffPane.setBounds(399, 0, 2, 600);
		cutoffPane.setBackground(Color.GREEN);
		this.add(cutoffPane);
		
		firstAllocButton = new JButton("Alloc");
		firstAllocText = new JTextField("0");
		firstKLabel = new JLabel("K");
		firstFreeButton = new JButton("Free");
		firstJobLabel = new JLabel("Job Num:", JLabel.CENTER);
		firstFreeText = new JTextField("0");

		firstAllocButton.setFont(font);
		firstAllocText.setFont(font);
		firstKLabel.setFont(font);
		firstFreeButton.setFont(font);
		firstJobLabel.setFont(font);
		firstFreeText.setFont(font);
		firstAllocText.setHorizontalAlignment(JTextField.RIGHT);

		// Add delegate
		firstAllocButton.addActionListener(firstFit);
		firstFreeButton.addActionListener(firstFit);
		firstAllocText.addKeyListener(firstFit);
		firstFreeText.addKeyListener(firstFit);

		firstPane.add(firstAllocButton);
		firstPane.add(firstAllocText);
		firstPane.add(firstKLabel);
		firstPane.add(firstFreeButton);
		firstPane.add(firstJobLabel);
		firstPane.add(firstFreeText);

		bestAllocButton = new JButton("Alloc");
		bestAllocText = new JTextField("0");
		bestKLabel = new JLabel("K");
		bestFreeButton = new JButton("Free");
		bestJobLabel = new JLabel("Job Num:", JLabel.CENTER);
		bestFreeText = new JTextField("0");

		bestAllocButton.setFont(font);
		bestAllocText.setFont(font);
		bestKLabel.setFont(font);
		bestFreeButton.setFont(font);
		bestJobLabel.setFont(font);
		bestFreeText.setFont(font);
		bestAllocText.setHorizontalAlignment(JTextField.RIGHT);

		// Add delegate
		bestAllocButton.addActionListener(bestFit);
		bestFreeButton.addActionListener(bestFit);
		bestAllocText.addKeyListener(bestFit);
		bestFreeText.addKeyListener(bestFit);
		
		bestPane.add(bestAllocButton);
		bestPane.add(bestAllocText);
		bestPane.add(bestKLabel);
		bestPane.add(bestFreeButton);
		bestPane.add(bestJobLabel);
		bestPane.add(bestFreeText);
		
		// Memory Block
		firstMemPane = new JPanel();
		bestMemPane = new JPanel();
		firstMemPane.setBounds(100, 60, 200, 320);
		bestMemPane.setBounds(500, 60, 200, 320);
		firstMemPane.setBackground(Color.GREEN);
		bestMemPane.setBackground(Color.GREEN);
		firstMemPane.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		bestMemPane.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		this.add(firstMemPane);
		this.add(bestMemPane);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setOpaque(false);
		this.add(contentPane);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
//		String cmd = e.getActionCommand();
//		System.out.println(cmd);
		if (e.getSource() == exitItem)
			System.exit(0);
		if ((JMenuItem)e.getSource() == aboutItem)
			JOptionPane.showMessageDialog(null, 
					"Boy Lee. All rights reserved.",
					"Copyright(c)",
					JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
}