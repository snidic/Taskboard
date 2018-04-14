import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class MainScreen {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Taskboard");
		JPanel topNav = new JPanel(new FlowLayout());
		TaskBoardModel board = new TaskBoardModel();

		JButton taskLoad = new JButton("Load Taskboard");
		taskLoad.addActionListener((event) -> {
			String file = JOptionPane.showInputDialog("Filename");
			// TODO: load file
		});

		JButton taskSave = new JButton("Save Taskboard");
		taskSave.addActionListener((event) -> {
			String file = JOptionPane.showInputDialog("Filename");
			// TODO: save file
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
}
