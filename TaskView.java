import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TaskView extends JDialog {

	private JTextField name;
	private JTextField desc;
	private JComboBox col;
	private String due;
	private Date date;
	private JPanel nameNav;
	private JPanel descNav;
	private JPanel colNav;
	private JPanel dueNav;
	private JPanel confirmNav;
	private TaskModel target;

	public TaskView(ProjectModel pr) {
		name = new JTextField(10);
		desc = new JTextField(20);
		col = new JComboBox(pr.getCols().toArray());
		due = "";
		nameNav = new JPanel();
		descNav = new JPanel();
		colNav = new JPanel();
		dueNav = new JPanel();
		confirmNav = new JPanel();

		GridLayout botLayout = new GridLayout(0, 1);
		botLayout.setHgap(5);
		botLayout.setVgap(5);
		colNav.setLayout(botLayout);
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.setResizable(false);
		this.setModal(true);
		this.setTitle("Create a Task");
	}

	public TaskView(ProjectModel pr, TaskModel ts) {
		name = new JTextField(10);
		name.setText(ts.getName());
		desc = new JTextField(20);
		desc.setText(ts.getDesc());
		col = new JComboBox(pr.getCols().toArray());
		due = "";
		due = ts.getDue().toString();
		nameNav = new JPanel();
		descNav = new JPanel();
		colNav = new JPanel();
		dueNav = new JPanel();
		confirmNav = new JPanel();
		target = ts;

		GridLayout botLayout = new GridLayout(0, 1);
		botLayout.setHgap(5);
		botLayout.setVgap(5);
		colNav.setLayout(botLayout);
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.setResizable(false);
		this.setModal(true);
		this.setTitle("Edit a Task");
	}

	private void createComponents() {
		// Name Component
		nameNav.add(new JLabel("Name: "));
		nameNav.add(name);

		// Desc Component
		descNav.add(new JLabel("Description:"));
		descNav.add(desc);

		// Column Component
		colNav.add(new JLabel("Status:"));
		colNav.add(col);

		updateDueNav();

		// Confirmation Component
		JButton accept = new JButton("Create");
		accept.addActionListener((event) -> {
			if (date == null)
				date = CalendarView.ENDLESS;
			target = new TaskModel(name.getText(), desc.getText(), date, col.getSelectedItem());
			this.dispose();
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener((event) -> {
			this.dispose();
		});
		confirmNav.add(accept);
		confirmNav.add(cancel);
	}

	private void updateDueNav() {
		dueNav.removeAll();

		dueNav.add(new JLabel("Due Date:"));
		JButton datePicker = new JButton("PICK");
		datePicker.addActionListener((event) -> {
			date = CalendarView.showPicker();
			updateDueNav();
			this.pack();
			this.revalidate();
			this.repaint();
		});
		dueNav.add(datePicker);
		if (date != null)
			due = date.toString();
		else
			due = "";
		dueNav.add(new JLabel(due));
	}

	private void addComponents() {
		// Adding JPanels
		this.add(nameNav);
		this.add(descNav);
		this.add(colNav);
		this.add(dueNav);
		this.add(confirmNav);
	}

	private void createDialog() {
		createComponents();
		addComponents();
	}

	public TaskModel showDialog() {
		createDialog();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);

		return target;
	}

	public static TaskModel showCreateDialog(ProjectModel project) {
		TaskView task = new TaskView(project);
		return task.showDialog();
	}

	public static TaskModel showEditDialog(ProjectModel project, TaskModel ts) {
		TaskView task = new TaskView(project, ts);
		return task.showDialog();
	}

}
