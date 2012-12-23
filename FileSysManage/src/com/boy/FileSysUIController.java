package com.boy;

import java.util.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class FileSysUIController implements ActionListener, TreeModelListener, TreeExpansionListener, TreeWillExpandListener, TreeSelectionListener, MouseListener, Serializable {
	private static final long serialVersionUID = 1L;
	
	private FileSysUIView fileSysUIView = null;
	private FileManage fileManage = null;
	private Map<DefaultMutableTreeNode, FileFCB> connectionMap = null;
	
	public FileSysUIController(FileSysUIView fileSysUIView, FileManage fileManage) {
		this.fileSysUIView = fileSysUIView;
		this.fileManage = fileManage;
		
		connectionMap = new HashMap<DefaultMutableTreeNode, FileFCB>();
		DefaultMutableTreeNode rootNode = fileSysUIView.rootNode;
		FileFCB rootFCB = fileManage.getRoot();
		connectionMap.put(rootNode, rootFCB);
		
		this.addListenerFromView();
	}
	
	public void addListenerFromView() {
		fileSysUIView.tree.addMouseListener(this);
		fileSysUIView.tree.addTreeSelectionListener(this);
		fileSysUIView.treeModel.addTreeModelListener(this);
		fileSysUIView.formatButton.addActionListener(this);
		fileSysUIView.newDirButton.addActionListener(this);
		fileSysUIView.newFileButton.addActionListener(this);
		fileSysUIView.deleteButton.addActionListener(this);
		fileSysUIView.renameButton.addActionListener(this);
		fileSysUIView.openButton.addActionListener(this);
		fileSysUIView.saveAndCloseButton.addActionListener(this);
	}
	
	public FileSysUIView getFileSysUIView() {
		return fileSysUIView;
	}

	public void setFileSysUIView(FileSysUIView fileSysUIView) {
		this.fileSysUIView = fileSysUIView;
	}
	public FileManage getFileManage() {
		return fileManage;
	}

	public void setFileManage(FileManage fileManage) {
		this.fileManage = fileManage;
	}

	public Map<DefaultMutableTreeNode, FileFCB> getConnectionMap() {
		return connectionMap;
	}

	public void setConnectionMap(Map<DefaultMutableTreeNode, FileFCB> connectionMap) {
		this.connectionMap = connectionMap;
	}

	// TODO ActionListener�ļ���
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == fileSysUIView.formatButton) {
			// TODO ������
			fileSysUIView.rootNode.removeAllChildren();
			fileSysUIView.tree.updateUI();
			fileManage.formatFileSystem();
			return;
		}
		
		DefaultMutableTreeNode selectedNode =
				(DefaultMutableTreeNode)fileSysUIView.tree.getLastSelectedPathComponent();
		if (selectedNode == null) {
			JOptionPane.showMessageDialog(null,
					"Please selected a file or a dirctory to operate!",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return ;
		}
		
		FileFCB selectedFCB = connectionMap.get(selectedNode);
		FCB_Type FCBType = selectedFCB.getFCBType();
		int ID = selectedFCB.getID();
		if (obj == fileSysUIView.newDirButton) {
			if (FCBType == FCB_Type.file) {
				JOptionPane.showMessageDialog(null,
						"Please selected a dirctory to new!",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (FCBType == FCB_Type.directory) {
				String dirName = this.getName(FCB_Type.directory);
				if (dirName.equals("N/A")) {
					return ;
				}
				FileFCB dirFCB = new FileFCB(dirName);
				Status_Type result = fileManage.createDir(dirName, ID, dirFCB);
				if (result == Status_Type.all_right) {
					DefaultMutableTreeNode newDir = new DefaultMutableTreeNode(dirName, true);
					selectedNode.add(newDir);
					TreeNode[] nodes = fileSysUIView.treeModel.getPathToRoot(newDir);
					TreePath path = new TreePath(nodes);
					fileSysUIView.tree.scrollPathToVisible(path);
					fileSysUIView.tree.updateUI();
					
					connectionMap.put(newDir, dirFCB);
				} else  {
					this.showStatus(result);
				}
			}
		} else if (obj == fileSysUIView.newFileButton) {
			if (FCBType == FCB_Type.file) {
				JOptionPane.showMessageDialog(null,
						"Please selected a dirctory to new!",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (FCBType == FCB_Type.directory) {
				String fileName = this.getName(FCB_Type.file);
				if (fileName.equals("N/A")) {
					return ;
				}
				FileFCB fileFCB = new FileFCB(fileName);
				Status_Type result = fileManage.createFile(fileName, ID, fileFCB);
				if (result == Status_Type.all_right) {
					DefaultMutableTreeNode newFile = new DefaultMutableTreeNode(fileName, false);
					selectedNode.add(newFile);
					TreeNode[] nodes = fileSysUIView.treeModel.getPathToRoot(newFile);
					TreePath path = new TreePath(nodes);
					fileSysUIView.tree.scrollPathToVisible(path);
					fileSysUIView.tree.updateUI();
					
					connectionMap.put(newFile, fileFCB);
				} else  {
					this.showStatus(result);
				}
			}
		} else if (obj == fileSysUIView.deleteButton) {
			if (selectedNode.getParent() == null) {
				JOptionPane.showMessageDialog(null,
						"Can't delete the root node!",
						"Delete Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				FileFCB toDeleteFCB = connectionMap.get(selectedNode);
				if (toDeleteFCB.getFCBType() == FCB_Type.directory) {
					fileManage.deleteDir(ID);
					recursiveDeleteNode(selectedNode);
					connectionMap.remove(selectedNode);
				} else if (toDeleteFCB.getFCBType() == FCB_Type.file) {
					fileManage.deleteFile(ID);
					connectionMap.remove(selectedNode);
				}
				fileSysUIView.treeModel.removeNodeFromParent(selectedNode);
			}
		} else if (obj == fileSysUIView.renameButton) {
			if (selectedNode.getParent() == null) {
				JOptionPane.showMessageDialog(null,
						"Can't rename the root node!",
						"Rename Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				FileFCB toRenameFCB = connectionMap.get(selectedNode);
				JOptionPane optionPane = new JOptionPane(
						"Please input the new name",
						JOptionPane.QUESTION_MESSAGE,
						JOptionPane.CANCEL_OPTION);
				optionPane.setInitialSelectionValue(toRenameFCB.getFileName());
				JDialog dialog = optionPane.createDialog("Rename");
				optionPane.setWantsInput(true);
				optionPane.setInputValue("N/A");
				dialog.setVisible(true);
				String newName = (String) optionPane.getInputValue();
				if (newName.equals("N/A"));
				Status_Type result = fileManage.rename(toRenameFCB.getID(), newName);

				if (result == Status_Type.all_right) {
					selectedNode.setUserObject(newName);
					fileSysUIView.tree.updateUI();
				} else {
					this.showStatus(result);
				}
			}
		} else if (obj == fileSysUIView.openButton) {
			if (selectedNode.getAllowsChildren()) {
				JOptionPane.showMessageDialog(null,
						"Please choose a file to open!",
						"Open Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				FileFCB toOpenFCB = connectionMap.get(selectedNode);
				fileSysUIView.textArea.setEditable(true);
				fileSysUIView.textArea.setText(fileManage.readFile(toOpenFCB.getID()));
			}
		} else if (obj == fileSysUIView.saveAndCloseButton) {
			if (selectedNode.getAllowsChildren()) {
				JOptionPane.showMessageDialog(null,
						"Please choose a file to open!",
						"Open Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				FileFCB toSaveFile = connectionMap.get(selectedNode);
				fileManage.saveFile(toSaveFile.getID(), fileSysUIView.textArea.getText());
				fileSysUIView.textArea.setEditable(false);
			}
		}
	}
	
	private void showStatus(Status_Type status) {
		if (status == Status_Type.dupilication_of_name) {
			JOptionPane.showMessageDialog(null,
					"Dupilication of name!",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (status == Status_Type.illegal_name) {
			JOptionPane.showMessageDialog(null,
					"Illegal_name!",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (status == Status_Type.memory_lack) {
			JOptionPane.showMessageDialog(null,
					"Memory lack!",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void recursiveDeleteNode(DefaultMutableTreeNode toDeleteNode) {
		Enumeration<DefaultMutableTreeNode> children = toDeleteNode.children();
		for (Enumeration<DefaultMutableTreeNode> e = children; children.hasMoreElements();) {
			DefaultMutableTreeNode tmpNode = e.nextElement();
			if (tmpNode.getChildCount() != 0) {
				recursiveDeleteNode(tmpNode);
			}
			connectionMap.remove(tmpNode);
		}
	}
	
	private String getName(FCB_Type FCBType) {
		JOptionPane optionPane = null;
		JDialog dialog = null;
		if (FCBType == FCB_Type.directory) {
			optionPane = new JOptionPane(
					"Please input the name of new directory",
					JOptionPane.QUESTION_MESSAGE,
					JOptionPane.CANCEL_OPTION);
			optionPane.setInitialSelectionValue("new_directory");
			dialog = optionPane.createDialog("New Directory");
		} else if (FCBType == FCB_Type.file) {
			optionPane = new JOptionPane(
					"Please input the name of new file",
					JOptionPane.QUESTION_MESSAGE,
					JOptionPane.CANCEL_OPTION);
			optionPane.setInitialSelectionValue("new_file");
			dialog = optionPane.createDialog("New File");
		}
		optionPane.setWantsInput(true);
		optionPane.setInputValue("N/A");
		dialog.setVisible(true);
		return (String)optionPane.getInputValue();
	}

	// TODO TreeModelListener�ļ���
	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {

	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		
	}
	// TODO TreeExpansionListener�ļ���
	@Override
	public void treeExpanded(TreeExpansionEvent event) {
		
	}

	@Override
	public void treeCollapsed(TreeExpansionEvent event) {
		
	}

	// TODO TreeWillExpandListener�ļ���
	@Override
	public void treeWillExpand(TreeExpansionEvent event)
			throws ExpandVetoException {
		
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event)
			throws ExpandVetoException {
		
	}

	// TODO TreeSelectionListener�ļ���
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		JTree tree = (JTree)e.getSource();
		// ���Ŀǰѡ�н��
		DefaultMutableTreeNode selectedNode =
				(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

		if (selectedNode == null)
			return ;
		if (selectedNode.getAllowsChildren()) {
			// TODO�����Ϣֱ�������TextArea��
		} else {
			FileFCB toShowFCB = connectionMap.get(selectedNode);
			fileSysUIView.textArea.setText(fileManage.readFile(toShowFCB.getID()));
			fileSysUIView.textArea.setEditable(false);
		}
		
		// �����Ĵ���
		if (selectedNode.isRoot()) {
			// TODO���ROOT����Ϣֱ�������TextArea��
		}
	}

	// TODO MouseLitener�ļ���
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// ͨ��TreePath��ȷ���ƶ��ڵ�
		TreePath treePath = fileSysUIView.tree.getPathForLocation(e.getX(), e.getY());
		if (treePath != null) {
			fileSysUIView.movePath = treePath;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) { // �����Ҫ�ϵ��ĸ��ڵ�
		TreePath treePath = fileSysUIView.tree.getPathForLocation(e.getX(), e.getY());
		if (treePath != null && fileSysUIView.movePath != null)
		{
			if (fileSysUIView.movePath.isDescendant(treePath) &&
				fileSysUIView.movePath != treePath) {
				// ������ƶ�
				JOptionPane.showMessageDialog(null,
						"Can't move a node to its child node!",
						"Move Error",
						JOptionPane.ERROR_MESSAGE);
				return ;
			} else if (fileSysUIView.movePath != treePath) { // �������ӽڵ��ƶ�������갴�¡��ɿ�ʱ����ͬһ���ڵ�  
				DefaultMutableTreeNode parentNode =
						(DefaultMutableTreeNode) treePath.getLastPathComponent();
				FileFCB parentFCB = connectionMap.get(parentNode);
				DefaultMutableTreeNode childNode = 
						(DefaultMutableTreeNode) fileSysUIView.movePath.getLastPathComponent();
				if (!parentNode.getAllowsChildren()) {
					JOptionPane.showMessageDialog(null,
							"Please choose a directory as the destination!",
							"Move Error",
							JOptionPane.ERROR_MESSAGE);
					return ;
				}
				FileFCB childFCB = connectionMap.get(childNode);
				Status_Type result = fileManage.move(childFCB.getID(), parentFCB.getID());
				if (result == Status_Type.all_right) {
					parentNode.add(childNode);
					fileSysUIView.movePath = null;
					fileSysUIView.tree.updateUI();
				} else {
					this.showStatus(result);
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}