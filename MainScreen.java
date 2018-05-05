import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class MainScreen extends JFrame {
	JFrame frame = new JFrame("Taskboard");
	JPanel topNav = new JPanel(new FlowLayout());
	JMenuBar menuBar = new JMenuBar();
	TaskBoardModel board = new TaskBoardModel();

	private void showMenuBar() {
		// File Section
		JMenu menu = new JMenu("File");
		menuBar.add(menu);

		JMenuItem menuItem = new JMenuItem("New");
		menuItem.addActionListener((event) -> {
			board = new TaskBoardModel();
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Open");
		menuItem.addActionListener((event) -> {
			// String file = JOptionPane.showInputDialog("Load Filename");
			// if (file != null) {
			// TaskBoardModel opened = openXML(file);
			// board = (opened != null) ? opened : board;
			//
			// }
			open();
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Save");
		menuItem.addActionListener((event) -> {
			save();
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Save as...");
		menuItem.addActionListener((event) -> {
			saveAs();
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Exit");
		menuItem.getAccessibleContext().setAccessibleDescription("desc");
		menuItem.addActionListener((event) -> {
			LoginView.showLoginDialog();
		});
		;
		menu.add(menuItem);

		// Project Section
		menu = new JMenu("Project");
		menuBar.add(menu);

		menuItem = new JMenuItem("Create Project");
		menuItem.getAccessibleContext().setAccessibleDescription("Makes a project");
		menuItem.addActionListener((event) -> {
			ProjectModel p = ProjectView.showCreateDialog(board);
			// TODO: Add the project to the TaskBoardModel
			board.addProject(p);
			System.out.println(p);
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Load Project");
		menuItem.getAccessibleContext().setAccessibleDescription("Makes a project");
		menu.add(menuItem);

		menuItem = new JMenuItem("Edit Project");
		menuItem.getAccessibleContext().setAccessibleDescription("Edit a project");
		menu.add(menuItem);

		// Task Section
		menu = new JMenu("Task");
		menuBar.add(menu);
		menuItem = new JMenuItem("Create Task");
		menuItem.addActionListener((event) -> {
			if (board.getActive() == null) {
				JOptionPane.showMessageDialog(null, "Create a Project First");
			} else {
				TaskModel t = TaskView.showCreateDialog(board.getActive());
				// TODO: Add the task to the ProjectModel
				board.getActive().addTask(t);
				System.out.println(t);
			}
		});
		menu.add(menuItem);

		menu = new JMenu("DEBUG");
		menuBar.add(menu);
		menuItem = new JMenuItem("Print Taskboard");
		menuItem.addActionListener((event) -> {
			System.out.println(board);
		});
		menu.add(menuItem);

		frame.setJMenuBar(menuBar);
	}

	public void showScreen() {
		showMenuBar();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		LoginView.showLoginDialog();
	}

	private void open() {
		JFileChooser fc = new JFileChooser(".");
		fc.addChoosableFileFilter(new XMLFilter());
		fc.setAcceptAllFileFilterUsed(false);

		int ret = fc.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			board = openXML(file);
		}
	}

	private void save() {
		if ("".equals(board.getFileName())) {

		} else
			saveAs();
	}

	private void saveAs() {
		JFileChooser fc = new JFileChooser(".");
		fc.addChoosableFileFilter(new XMLFilter());
		fc.setAcceptAllFileFilterUsed(false);

		int ret = fc.showSaveDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if (!file.getName().contains(".xml")) {
				String ext = file.getAbsolutePath() + ".xml";
				file = new File(ext);
			}
			saveXML(file);
		}
	}

	private TaskBoardModel openXML(File file) {
		try {
			// TODO: Load File
			XMLDecoder xmlIn = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
			TaskBoardModel load = (TaskBoardModel) xmlIn.readObject();
			xmlIn.close();
			JOptionPane.showMessageDialog(this, "Loaded " + file);
			return load;
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "File Not Loaded: " + file);
		}
		return null;
	}

	private void saveXML(File file) {
		try {
			// TODO: Save File
			file.renameTo(new File(file.getName() + ".xml"));
			XMLEncoder xmlOut = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
			xmlOut.writeObject(board);
			xmlOut.close();
			JOptionPane.showMessageDialog(this, "Saved " + file);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "File Not Saved: " + file);
		}
	}

	public static void main(String[] args) {
		MainScreen ms = new MainScreen();
		ms.showScreen();
		// CalendarView.showPicker();
	}

	private class XMLFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			if (f.isDirectory())
				return true;

			String extension = f.getName();

			int dot = extension.lastIndexOf(".");
			if (dot >= 0)
				extension = extension.substring(dot);

			if (extension != null)
				if (extension.equals(".xml"))
					return true;

			return false;
		}

		@Override
		public String getDescription() {
			return "XML Only";
		}

	}

}
