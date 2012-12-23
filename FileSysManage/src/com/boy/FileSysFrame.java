package com.boy;

import java.io.*;
import java.awt.event.*;
import javax.swing.*;

public class FileSysFrame extends JFrame implements WindowListener, ActionListener {
	private static final long serialVersionUID = 1L;
	
	private FileSysUIController fileSysUIController = null;
	
	private JMenuBar menuBar = null;
	private JMenu fileMenu, helpMenu = null;
	private JMenuItem exitMenuItem = null, helpMenuItem = null, aboutMenuItem = null;
	
	public FileSysFrame(FileSysUIController fileSysUIController) {
		super("File System Management");
		this.setSize(Constants.FRAME_LENGTH, Constants.FRAME_WIDTH);
		this.fileSysUIController = this.get();
		if (this.fileSysUIController == null) {
			this.fileSysUIController = fileSysUIController;
		}

		// Create menu
		menuBar = new JMenuBar();

		fileMenu = new JMenu("File(F)");
		helpMenu = new JMenu("Help(H)");
		fileMenu.setMnemonic('F');
		helpMenu.setMnemonic('H');
		
		exitMenuItem = new JMenuItem("exit(E)");
		helpMenuItem = new JMenuItem("help(H)");
		aboutMenuItem = new JMenuItem("about(A)");
		exitMenuItem.setMnemonic('E');
		helpMenuItem.setMnemonic('H');
		aboutMenuItem.setMnemonic('A');
		
		// Add ActionListener
		exitMenuItem.addActionListener(this);
		helpMenuItem.addActionListener(this);
		aboutMenuItem.addActionListener(this);
	
		// Add to frame
		fileMenu.add(exitMenuItem);
		helpMenu.add(helpMenuItem);
		helpMenu.add(aboutMenuItem);
		
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		// Add to Listener
		this.addWindowListener(this);
		
		this.setJMenuBar(menuBar);
		// TODO ���ô��ڳ�ʼλ��
		this.setLocation(100, 100);
		this.setResizable(false);
		this.setVisible(true);
		this.add(this.fileSysUIController.getFileSysUIView());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object event = e.getSource();
		if (event == exitMenuItem) {
			System.exit(0);
		} else if (event == helpMenuItem) {
			JOptionPane.showMessageDialog(null,
					"It's easy to use~~~",
					"Help",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (event == aboutMenuItem) {
			JOptionPane.showMessageDialog(null,
					"Boy Lee. All rights reserved.",
					"Copyright(c)",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public FileSysUIController getFileSysUIController() {
		return fileSysUIController;
	}

	public void setFileSysUIController(FileSysUIController fileSysUIController) {
		this.fileSysUIController = fileSysUIController;
	}

	// TODO window�ļ���
	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.save();
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}
	
	private void save() {
//		byte[] buffer = new byte[Constants.MEMORY_SIZE];
		File f = new File("data.dat");
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(f, false);
			oos = new ObjectOutputStream(fos);
//			oos.write(buffer);
			oos.writeObject(fileSysUIController);
			fos.close();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private FileSysUIController get() {
		File f = new File("data.dat");
		FileSysUIController tmpFileSysUIController = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(f);
			ois =  new ObjectInputStream(fis);
			tmpFileSysUIController = (FileSysUIController)ois.readObject();
			fis.close();
			ois.close();
		} catch (FileNotFoundException e) {
			tmpFileSysUIController = null;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			tmpFileSysUIController = null;
		}
		return tmpFileSysUIController;
	}
}