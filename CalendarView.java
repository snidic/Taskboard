import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CalendarView extends JDialog {

	private JPanel monthNav;
	private JPanel dateNav;
	private JPanel confirmNav;
	JLabel targetDate;
	private Calendar c;
	private Date target;
	public static final Date ENDLESS = new Date(Long.MAX_VALUE);

	public CalendarView() {
		monthNav = new JPanel();
		dateNav = new JPanel();
		confirmNav = new JPanel();
		targetDate = new JLabel();
		c = Calendar.getInstance();
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);

		GridLayout dateLayout = new GridLayout(0, 7);
		dateLayout.setHgap(5);
		dateLayout.setVgap(5);
		dateNav.setLayout(dateLayout);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setModal(true);
		this.setTitle("Choose a date");
	}

	private void createComponents() {
		updateDate();
		updateConfirm();
	}

	private void updateDate() {
		monthNav.removeAll();
		dateNav.removeAll();

		// Month Component
		JButton back = new JButton("<-");
		back.addActionListener((event) -> {
			c.add(Calendar.MONTH, -1);
			updateDate();
			this.pack();
			this.revalidate();
			this.repaint();
		});
		JButton next = new JButton("->");
		next.addActionListener((event) -> {
			c.add(Calendar.MONTH, 1);
			updateDate();
			this.pack();
			this.revalidate();
			this.repaint();
		});

		monthNav.add(back);
		monthNav.add(new JLabel(intMonthToString(c.get(Calendar.MONTH))));
		monthNav.add(next);

		// Date Component
		dateNav.add(new JLabel("S"));
		dateNav.add(new JLabel("M"));
		dateNav.add(new JLabel("T"));
		dateNav.add(new JLabel("W"));
		dateNav.add(new JLabel("T"));
		dateNav.add(new JLabel("F"));
		dateNav.add(new JLabel("S"));

		// Date offset
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, c.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, c.get(Calendar.MONTH));
		cal.set(Calendar.DATE, 1);
		for (int i = 1; i < cal.get(Calendar.DAY_OF_WEEK); i++) {
			dateNav.add(new JLabel(" "));
		}
		// Date numbers
		for (int i = 1; i <= c.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			JButton date = new JButton(new Integer(i).toString());
			Calendar fill = Calendar.getInstance();
			fill.set(Calendar.YEAR, c.get(Calendar.YEAR));
			fill.set(Calendar.MONTH, c.get(Calendar.MONTH));
			fill.set(Calendar.DATE, i);

			date.addActionListener((event) -> {
				Date temp = fill.getTime();
				temp.setHours(0);
				temp.setMinutes(0);
				temp.setSeconds(0);
				target = temp;
				updateConfirm();
				this.pack();
				this.revalidate();
				this.repaint();
			});
			dateNav.add(date);
		}
	}

	private void updateConfirm() {
		confirmNav.removeAll();
		// Confirmation Component
		JButton accept = new JButton("Create");
		accept.addActionListener((event) -> {
			this.dispose();
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener((event) -> {
			target = null;
			this.dispose();
		});

		String str = "";
		if (target != null)
			str = TaskModel.dueToString(target);
		JLabel targetDate = new JLabel(str);

		confirmNav.add(accept);
		confirmNav.add(targetDate);
		confirmNav.add(cancel);
	}

	private String intMonthToString(int month) {
		switch (month) {
		case Calendar.JANUARY:
			return "JANUARY";
		case Calendar.FEBRUARY:
			return "FEBRUARY";
		case Calendar.MARCH:
			return "MARCH";
		case Calendar.APRIL:
			return "APRIL";
		case Calendar.MAY:
			return "MAY";
		case Calendar.JUNE:
			return "JUNE";
		case Calendar.JULY:
			return "JULY";
		case Calendar.AUGUST:
			return "AUGUST";
		case Calendar.SEPTEMBER:
			return "SEPTEMBER";
		case Calendar.OCTOBER:
			return "OCTOBER";
		case Calendar.NOVEMBER:
			return "NOVEMBER";
		default:
			return "DECEMBER";
		}
	}

	private void addComponents() {
		// Adding JPanels
		this.add(monthNav, BorderLayout.NORTH);
		this.add(dateNav, BorderLayout.CENTER);
		this.add(confirmNav, BorderLayout.SOUTH);
	}

	private void createDialog() {
		createComponents();
		addComponents();
	}

	public Date showDialog() {
		createDialog();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
		return target;
	}

	public static Date showPicker() {
		CalendarView cal = new CalendarView();
		return cal.showDialog();
	}

}
