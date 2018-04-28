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
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.*;

public class MainScreen extends JFrame {
	JFrame frame = new JFrame("Taskboard");
	JPanel topNav = new JPanel(new FlowLayout());
	TaskBoardModel board = new TaskBoardModel();

	public void showScreen() {
		JButton taskLoad = new JButton("Load Taskboard");
		taskLoad.addActionListener((event) -> {
			String file = JOptionPane.showInputDialog("Load Filename");
			if (file != null) {
				TaskBoardModel opened = openXML(file);
				board = (opened != null) ? opened : board;

			}
		});

		JButton taskSave = new JButton("Save Taskboard");
		taskSave.addActionListener((event) -> {
			String file = JOptionPane.showInputDialog("Save Filename");
			if (file != null) {
				saveXML(file);
			}
		});

		JButton projAdd = new JButton("Create \nProject");
		projAdd.addActionListener((event) -> {
			ProjectModel p = ProjectView.showCreateDialog(board);
			// TODO: Add the project to the TaskBoardModel
			board.addProject(p);
			System.out.println(p);
		});
		JButton taskAdd = new JButton("Create \nTask");
		taskAdd.addActionListener((event) -> {
			if (board.getActive() == null) {
				JOptionPane.showMessageDialog(null, "Create a Project First");
			} else {
				TaskModel t = TaskView.showCreateDialog(board.getActive());
				// TODO: Add the task to the ProjectModel
				board.getActive().addTask(t);
				System.out.println(t);
			}
		});
		JButton logout = new JButton("Logout");
		logout.addActionListener((event) -> {
			LoginView.showLoginDialog();
		});

		// TO BE DELETED
		JButton print = new JButton("PRINT TASKBOARD");
		print.addActionListener((event) -> {
			System.out.println(board);
		});

		topNav.add(taskSave);
		topNav.add(taskLoad);
		topNav.add(taskAdd);
		topNav.add(projAdd);
		topNav.add(logout);
		topNav.add(print);

		frame.add(topNav);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		LoginView.showLoginDialog();
	}

	public TaskBoardModel openXML(String file) {
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

	public void saveXML(String file) {
		try {
			// TODO: Save File
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
		//CalendarView.showPicker();
	}

}
