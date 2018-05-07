import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class LoginView extends JDialog {
	private JTextField userField;
	private JTextField passField;
	private final String USERNAME = "admin";
	private final String PASSWORD = "admin";
	private JPanel nameNav;
	private JPanel userNav;
	private JPanel errorNav;
	private JPanel confirmNav;

	public LoginView() {
		// Set up
		nameNav = new JPanel();
		userNav = new JPanel();
		errorNav = new JPanel();
		confirmNav = new JPanel();
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.setResizable(false);
		this.setModal(true);
		this.setTitle("Login");
		// Username component
		userField = new JTextField(10);
		passField = new JTextField(10);
		nameNav.add(new JLabel("Username"));
		nameNav.add(userField);

		// Password component
		userNav.add(new JLabel("Password"));
		userNav.add(passField);

		// Error component
		errorNav.add(new JLabel(" "));

		WindowAdapter adapt = new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		};

		addWindowListener(adapt);

		// Confirmation component
		JButton confirm = new JButton("Login");
		confirm.addActionListener((event) -> {
			if (!confirm()) {
				errorNav.removeAll();
				errorNav.add(new JLabel("Invalid Username/Password"));
				this.pack();
				this.revalidate();
				this.repaint();
			} else {
				removeWindowListener(adapt);
				this.dispose();
			}
		});
		confirmNav.add(confirm);

		// Adding Components
		this.add(nameNav);
		this.add(userNav);
		this.add(errorNav);
		this.add(confirmNav);
	}

	/**
	 * Tests to see if the inputed username and password are valid
	 * 
	 * @return true if credentials are correct, false otherwise
	 */
	public boolean confirm() {
		String userText = userField.getText();
		String passText = passField.getText();

		return !(!USERNAME.equals(userText) || !PASSWORD.equals(passText));
	}

	public static void showLoginDialog() {
		LoginView login = new LoginView();
		login.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		login.pack();
		login.setLocationRelativeTo(null);
		login.setVisible(true);
	}

}
