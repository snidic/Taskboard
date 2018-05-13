import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
	JFrame frame = new JFrame("Untitled");
	JPanel topNav = new JPanel(new FlowLayout());
	JMenuBar menuBar = new JMenuBar();
	TaskBoardModel board = new TaskBoardModel();
	TaskBoardView view = new TaskBoardView();
	boolean unsaved = false;

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
			boolean cont = true;
			if (unsaved)
				cont = savePrompt();
			if (cont)
				newTaskBoard();
		});
		menu.add(menuItem);

		// Opens a TaskBoardModel from a xml file
		menuItem = new JMenuItem("Open");
		menuItem.addActionListener((event) -> {
			boolean cont = true;
			if (unsaved)
				cont = savePrompt();
			if (cont)
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
			boolean cont = true;
			if (unsaved)
				cont = savePrompt();
			if (cont) {
				newTaskBoard();
				LoginView.showLoginDialog();
			}
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
			createProject();
		});
		menu.add(menuItem);

		// Changes active project
		menuItem = new JMenuItem("Load Project");
		menuItem.getAccessibleContext().setAccessibleDescription("Loads a project");
		menuItem.addActionListener((event) -> {
			loadProject();
		});
		menu.add(menuItem);

		// Edits the current project
		menuItem = new JMenuItem("Edit Project");
		menuItem.getAccessibleContext().setAccessibleDescription("Edit a project");
		menuItem.addActionListener((event) -> {
			editProject();
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Delete Project");
		menuItem.getAccessibleContext().setAccessibleDescription("Deletes current project");
		menuItem.addActionListener((event) -> {
			deleteProject();
		});
		menu.add(menuItem);

		/// Task Section ///
		menu = new JMenu("Task");
		menuBar.add(menu);

		// Creates a task to the active project
		menuItem = new JMenuItem("Create Task");
		menuItem.addActionListener((event) -> {
			createTask();
		});
		menu.add(menuItem);

		// Edits a task from the active project
		menuItem = new JMenuItem("Edit Task");
		menuItem.addActionListener((event) -> {
			editTask();
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Delete Task");
		menuItem.addActionListener((event) -> {
			deleteTask();
		});
		menu.add(menuItem);

		frame.setJMenuBar(menuBar);
	}

	private void showButtons() {
		JPanel buttonNav = new JPanel();
		buttonNav.setLayout(new BoxLayout(buttonNav, BoxLayout.LINE_AXIS));
		buttonNav.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JButton button = new JButton("Load Project");
		button.addActionListener((event) -> {
			loadProject();
		});
		buttonNav.add(button);

		button = new JButton("Edit Project");
		button.addActionListener((event) -> {
			editProject();
		});
		buttonNav.add(button);

		button = new JButton("Create Task");
		button.addActionListener((event) -> {
			createTask();
		});
		buttonNav.add(button);

		button = new JButton("Edit Task");
		button.addActionListener((event) -> {
			editTask();
		});
		buttonNav.add(button);

		frame.add(buttonNav);
	}

	/**
	 * Shows the MainScreen Frame
	 */
	public void showScreen() {
		showMenuBar();
		showButtons();

		frame.add(view);

		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		LoginView.showLoginDialog();
	}

	private void updateFrame() {
		frame.pack();
		frame.revalidate();
		frame.repaint();
	}

	private void updateView() {
		view.removeAll();
		view.draw();
		view.revalidate();
		updateFrame();
	}

	private void loadView(ProjectModel pr) {
		view.setProject(pr);
		updateView();
	}

	private boolean savePrompt() {
		Object[] options = { "Save", "Don't Save", "Cancel" };
		String title = board.getFileName();
		if (title == null)
			title = "untitled";
		int n = JOptionPane.showOptionDialog(frame, title + " has unsaved changes. What would you like to do?",
				"Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
				options[2]);
		if (n == 2)
			return false;
		if (n == 0)
			save();
		return true;
	}

	private void setUnsaved(boolean to) {
		unsaved = to;
		String title = frame.getTitle();
		int ind = title.lastIndexOf("*");

		if (unsaved) {
			if (ind < 0)
				frame.setTitle(frame.getTitle() + "*");
		} else {
			if (ind > 0) {
				title.substring(0, ind);
			}
			frame.setTitle(title);
		}
	}

	private void newTaskBoard() {
		frame.setTitle("Untitled");
		setUnsaved(false);
		board = new TaskBoardModel();
		loadView(board.getActive());
	}

	private void createProject() {
		ProjectModel p = ProjectView.showCreateDialog(board);
		if (p != null) {
			setUnsaved(true);
			board.addProject(p);
			board.setActive(p);
			loadView(board.getActive());
		}
	}

	private void loadProject() {
		Object[] choices = board.getProjects().toArray();
		if (choices.length > 0) {
			ProjectModel p = (ProjectModel) JOptionPane.showInputDialog(this, "Choose a project to load",
					"Load Project", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
			if ((p != null)) {
				board.setActive(p);
				updateFrame();
				loadView(p);
			}
		} else {
			JOptionPane.showMessageDialog(null, "No projects to load");
		}
	}

	private void editProject() {
		if (board.getActive() == null) {
			JOptionPane.showMessageDialog(null, "Create a Project First");
		} else {
			ProjectModel p = ProjectView.showEditDialog(board, board.getActive());
			setUnsaved(true);
			board.getActive().setAs(p);
			loadView(p);
		}
	}

	private void deleteProject() {
		if (board.getActive() != null) {
			Object[] options = { "Yes", "No" };
			int n = JOptionPane.showOptionDialog(this, "Are you sure you wish to delete the current project?",
					"Delete Project", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
					options[1]);
			if (n == 0) {
				int index = board.getProjects().indexOf(board.getActive());
				board.getProjects().remove(index);
				board.setActive(null);
				loadView(board.getActive());
				if (board.getProjects().size() > 0) {
					loadProject();
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "No project loaded");
		}
	}

	private void createTask() {
		if (board.getActive() == null) {
			JOptionPane.showMessageDialog(null, "Create a Project First");
		} else {
			TaskModel t = TaskView.showCreateDialog(board.getActive());
			if (t != null) {
				setUnsaved(true);
				board.getActive().addTask(t);
				updateView();
			}
		}
	}

	private void editTask() {
		if (board.getActive() == null || board.getActive().getTasks().size() <= 0) {
			JOptionPane.showMessageDialog(null, "Create a Task First");
		} else {

			// Choose Task
			Object[] choices = board.getActive().getTasks().toArray();
			if (choices.length > 0) {
				TaskModel t = (TaskModel) JOptionPane.showInputDialog(this, "Choose a task to edit", "Edit Task",
						JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				if ((t != null)) {
					int index = board.getActive().getTasks().indexOf(t);

					t = TaskView.showEditDialog(board.getActive(), t);
					setUnsaved(true);
					board.getActive().getTasks().set(index, t);
					updateFrame();
					updateView();
				}
			} else {
				JOptionPane.showMessageDialog(null, "No tasks to load");
			}
		}
	}

	private void deleteTask() {
		if (board.getActive() == null || board.getActive().getTasks().size() <= 0) {
			JOptionPane.showMessageDialog(null, "No tasks to delete");
		} else {

			// Choose Task
			Object[] choices = board.getActive().getTasks().toArray();
			if (choices.length > 0) {
				TaskModel t = (TaskModel) JOptionPane.showInputDialog(this, "Choose a task to delete", "Delete Task",
						JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				if ((t != null)) {

					Object[] options = { "Yes", "No" };
					int n = JOptionPane.showOptionDialog(this, "Are you sure you wish to delete the selected task?",
							"Delete Project", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
							options, options[1]);
					if (n == 0) {
						int index = board.getActive().getTasks().indexOf(t);
						board.getActive().getTasks().remove(index);
						setUnsaved(true);
						updateView();
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "No tasks to load");
			}
		}
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
			if (file.exists()) {
				board = openXML(file);
				board.setFileName(file.toString()); // Set path to opened path
				loadView(board.getActive());
			} else {
				JOptionPane.showMessageDialog(this, "Unknown File Specified");
			}
		}
	}

	/**
	 * Saves TaskBoardModel if possible, else calls saveAs()
	 */
	private void save() {
		if (board.getFileName() == null) {
			saveAs();
		} else {
			saveXML(new File(board.getFileName()));
		}
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
			// Append .xml
			if (!file.getName().contains(".xml")) {
				String ext = file.getAbsolutePath() + ".xml";
				file = new File(ext);
			}
			board.setFileName(file.toString());
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
			frame.setTitle(file.getName());
			setUnsaved(false);
			JOptionPane.showMessageDialog(this, "Loaded " + file);
			return load;
		} catch (Exception e) {
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
			file = new File(file.toString());
			XMLEncoder xmlOut = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
			xmlOut.writeObject(board);
			xmlOut.close();

			frame.setTitle(file.getName());
			setUnsaved(false);
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
