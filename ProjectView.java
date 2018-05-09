import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ProjectView extends JDialog {

	private JTextField name;
	private JButton create;
	private ArrayList<JTextField> cols;
	private JPanel nameNav;
	private JPanel addNav;
	private JPanel colNav;
	private JPanel confirmNav;
	private JPanel errorNav;
	private ProjectModel target;
	private TaskBoardModel board;

	public ProjectView(TaskBoardModel tb, ProjectModel pr) {
		// Name Components
		name = new JTextField(10);
		name.setText(pr.getName());
		cols = new ArrayList<>();
		for (String col : pr.getCols()) {
			JTextField colField = new JTextField(10);
			colField.setText(col);
			cols.add(colField);
		}
		nameNav = new JPanel();
		addNav = new JPanel();
		colNav = new JPanel();
		confirmNav = new JPanel();
		errorNav = new JPanel();
		target = pr;
		board = tb;
		GridLayout botLayout = new GridLayout(0, 4);
		botLayout.setHgap(5);
		botLayout.setVgap(5);
		colNav.setLayout(botLayout);
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.setResizable(false);
		this.setModal(true);
		this.setTitle("Edit a Project");
	}

	public ProjectView(TaskBoardModel tb) {
		// Name Components
		name = new JTextField(10);
		cols = new ArrayList<>();
		cols.add(new JTextField(10));
		nameNav = new JPanel();
		addNav = new JPanel();
		colNav = new JPanel();
		confirmNav = new JPanel();
		errorNav = new JPanel();
		board = tb;
		GridLayout botLayout = new GridLayout(0, 4);
		botLayout.setHgap(5);
		botLayout.setVgap(5);
		colNav.setLayout(botLayout);
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.setResizable(false);
		this.setModal(true);
		this.setTitle("Create a Project");
	}

	private void createDialog() {
		createComponents();
		addComponents();
	}

	private void createComponents() {
		// Name Component
		nameNav.add(new JLabel("Name: "));
		nameNav.add(name);

		// Add Col Component
		addNav.add(new JLabel("Columns: "));
		create = new JButton("+");
		create.addActionListener((event) -> {
			cols.add(new JTextField(10));
			updateCols();
			this.pack();
			this.revalidate();
			this.repaint();
		});
		addNav.add(create);

		// Column Component
		updateCols();

		// Confirmation Component
		JButton accept = new JButton("Create");
		accept.addActionListener((event) -> {
			if ((target != null && !target.getName().equals(name.getText()))
					&& board.containsProjectName(name.getText())) {
				addError("Duplicate Project Name Found");
			} else if (!verify()) {
				addError("Duplicate Column Name Found");
			} else if (cols.isEmpty()) {
				addError("No Columns are added");
			} else {
				if (target == null)
					target = new ProjectModel(name.getText(), colsToString());
				else
					target = new ProjectModel(name.getText(), colsToString(), target.getTasks());
				this.dispose();
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener((event) -> {
			this.dispose();
		});
		confirmNav.add(accept);
		confirmNav.add(cancel);
	}

	private void addError(String error) {
		errorNav.removeAll();
		errorNav.add(new JLabel(error));
		this.pack();
		this.revalidate();
		this.repaint();
	}

	private void addComponents() {
		// Adding JPanels
		this.add(nameNav);
		this.add(addNav);
		this.add(colNav);
		this.add(errorNav);
		this.add(confirmNav);
	}

	/**
	 * For every column, create a corresponding remove button
	 */
	private void updateCols() {
		colNav.removeAll();
		// Add all columns
		for (JTextField field : cols) {
			colNav.add(field);
			int ind = cols.indexOf(field);
			// Add a remove button along with each column
			JButton b = new JButton("-");
			b.addActionListener((event) -> {
				cols.remove(ind);
				updateCols();
				this.pack();
				this.revalidate();
				this.repaint();
			});
			colNav.add(b);

			/// Swapping columns ///
			// Move up column
			if (ind > 0 && ind <= cols.size() - 1) {
				b = new JButton("^");
				b.addActionListener((event) -> {
					swapCols(ind, -1);
					updateCols();
					this.pack();
					this.revalidate();
					this.repaint();
				});
				colNav.add(b);
			} else {
				JLabel l = new JLabel();
				colNav.add(l);
			}
			// Move down column
			if (ind < cols.size() - 1 && ind >= 0) {
				b = new JButton("v");
				b.addActionListener((event) -> {
					swapCols(ind, 1);
					updateCols();
					this.pack();
					this.revalidate();
					this.repaint();
				});
				colNav.add(b);
			} else {
				JLabel l = new JLabel();
				colNav.add(l);
			}
		}
	}

	private void swapCols(int ind, int dir) {
		JTextField temp = cols.get(ind);
		cols.set(ind, cols.get(ind + dir));
		cols.set(ind + dir, temp);
	}

	/**
	 * Checks to see if columns have unique names
	 * 
	 * @return true if all names are unique, false otherwise
	 */
	private boolean verify() {
		ArrayList<String> labels = new ArrayList<String>();
		for (JTextField field : cols) {
			String txt = field.getText();
			if (labels.contains(txt))
				return false;
			labels.add(txt);
		}
		return true;
	}

	/**
	 * @return inputed strings for the column fields
	 */
	private ArrayList<String> colsToString() {
		ArrayList<String> labels = new ArrayList<String>();
		for (JTextField field : cols) {
			String txt = field.getText();
			labels.add(txt);
		}
		return labels;
	}

	public ProjectModel showDialog() {
		createDialog();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);

		return target;
	}

	/**
	 * Creates and shows a new ProjectView dialog. Use for creating a new
	 * ProjectModel.
	 * 
	 * @param board
	 *            - TaskBoardModel being currently used
	 * @return ProjectModel to be added
	 */
	public static ProjectModel showCreateDialog(TaskBoardModel board) {
		ProjectView proj = new ProjectView(board);
		return proj.showDialog();
	}

	/**
	 * Creates and shows a new ProjectView dialog. Use for editing a current
	 * ProjectModel.
	 * 
	 * @param board
	 *            - TaskBoardModel being currently used
	 * @param project
	 *            - ProjectModel being edited
	 * @return ProjectModel to be added
	 */
	public static ProjectModel showEditDialog(TaskBoardModel board, ProjectModel project) {
		ProjectView proj = new ProjectView(board, project);
		return proj.showDialog();
	}
}
