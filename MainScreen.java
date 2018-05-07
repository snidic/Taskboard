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
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class MainScreen extends JFrame {
	JFrame frame = new JFrame("Taskboard");
	JPanel topNav = new JPanel(new FlowLayout());
	JMenuBar menuBar = new JMenuBar();
	TaskBoardModel board = new TaskBoardModel();

	/**
	 * Shows the top menu bar {File, Project, Task, etc...}
	 */
	private void showMenuBar() {
		// File Section ///
		JMenu menu = new JMenu("File");
		menuBar.add(menu);

		// Creates a new TaskBoardModel
		JMenuItem menuItem = new JMenuItem("New");
		menuItem.addActionListener((event) -> {
			board = new TaskBoardModel();
		});
		menu.add(menuItem);

		// Opens a TaskBoardModel from a xml file
		menuItem = new JMenuItem("Open");
		menuItem.addActionListener((event) -> {
			open();
		});
		menu.add(menuItem);

		// Saves the current TaskBoardModel into the current xml file
		// if not possible, creates a new one
		menuItem = new JMenuItem("Save");
		menuItem.addActionListener((event) -> {
			save();
		});
		menu.add(menuItem);

		// Saves the current TaskBoardModel into a xml file
		menuItem = new JMenuItem("Save as...");
		menuItem.addActionListener((event) -> {
			saveAs();
		});
		menu.add(menuItem);

		// Shows Login Prompt
		menuItem = new JMenuItem("Exit");
		menuItem.getAccessibleContext().setAccessibleDescription("desc");
		menuItem.addActionListener((event) -> {
			LoginView.showLoginDialog();
		});
		;
		menu.add(menuItem);

		/// Project Section ///
		menu = new JMenu("Project");
		menuBar.add(menu);

		// Creates a new Project to the TaskBoardModel
		menuItem = new JMenuItem("Create Project");
		menuItem.getAccessibleContext().setAccessibleDescription("Makes a project");
		menuItem.addActionListener((event) -> {
			ProjectModel p = ProjectView.showCreateDialog(board);
			board.addProject(p);
		});
		menu.add(menuItem);

		// Changes active project
		menuItem = new JMenuItem("Load Project");
		menuItem.getAccessibleContext().setAccessibleDescription("Loads a project");
		menuItem.addActionListener((event) -> {

			Object[] choices = board.getProjects().toArray();
			if (choices.length > 0) {
				ProjectModel p = (ProjectModel) JOptionPane.showInputDialog(this, "Choose a project to load",
						"Load Project", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				if ((p != null)) {
					board.setActive(p);
					this.pack();
					this.revalidate();
					this.repaint();
				}
			} else {
				JOptionPane.showMessageDialog(null, "No projects to load");
			}
		});
		menu.add(menuItem);

		// Edits the current project
		menuItem = new JMenuItem("Edit Project");
		menuItem.getAccessibleContext().setAccessibleDescription("Edit a project");
		menuItem.addActionListener((event) -> {
			if (board.getActive() == null) {
				JOptionPane.showMessageDialog(null, "Create a Project First");
			} else {
				ProjectModel p = ProjectView.showEditDialog(board, board.getActive());
				board.getActive().setAs(p);
			}
		});
		menu.add(menuItem);

		/// Task Section ///
		menu = new JMenu("Task");
		menuBar.add(menu);

		// Creates a task to the active project
		menuItem = new JMenuItem("Create Task");
		menuItem.addActionListener((event) -> {
			if (board.getActive() == null) {
				JOptionPane.showMessageDialog(null, "Create a Project First");
			} else {
				TaskModel t = TaskView.showCreateDialog(board.getActive());
				board.getActive().addTask(t);
			}
		});
		menu.add(menuItem);

		// Edits a task from the active project TODO:
		menuItem = new JMenuItem("Edit Task");
		menuItem.addActionListener((event) -> {
			if (board.getActive() == null || board.getActive().getTasks().size() <= 0) {
				JOptionPane.showMessageDialog(null, "Create a Task First");
			} else {
				// TaskModel t = TaskView.showCreateDialog(board.getActive());
				// // TODO: Add the task to the ProjectModel
				// board.getActive().addTask(t);
				// System.out.println(t);
			}
		});
		menu.add(menuItem);

		// TODO: REMOVE
		menu = new JMenu("DEBUG");
		menuBar.add(menu);
		menuItem = new JMenuItem("Print Taskboard");
		menuItem.addActionListener((event) -> {
			System.out.println(board);
		});
		menu.add(menuItem);

		frame.setJMenuBar(menuBar);
	}

	/**
	 * Shows the MainScreen Frame
	 */
	public void showScreen() {
		showMenuBar();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		LoginView.showLoginDialog();
	}

	/**
	 * Opens file chooser prompt to open a TaskBoardModel
	 */
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

	/**
	 * Saves TaskBoardModel if possible, else calls saveAs()
	 */
	private void save() {
		if ("".equals(board.getFileName())) {

		} else
			saveAs();
	}

	/**
	 * Opens file chooser prompt to save a TaskBoardModel
	 */
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

	/**
	 * Serialize
	 */
	private TaskBoardModel openXML(File file) {
		try {
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

	/**
	 * Deserialize
	 */
	private void saveXML(File file) {
		try {
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
	}

	/**
	 * Class for file chooser filter
	 */
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
