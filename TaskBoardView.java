import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class TaskBoardView extends JPanel {
	ProjectModel pr;

	public TaskBoardView() {
		this.setLayout(new FlowLayout());
	}

	public TaskBoardView(ProjectModel pr) {
		this.pr = pr;
		this.setLayout(new FlowLayout());
	}

	public void setProject(ProjectModel pr) {
		this.pr = pr;
	}

	public void draw() {
		if (pr == null) {
			Border pane = BorderFactory.createTitledBorder("");
			setBorder(pane);
			return;
		}
		Border pane = BorderFactory.createTitledBorder(pr.getName());
		setBorder(pane);

		for (String s : pr.getCols()) {
			JPanel col = new ColView(s);

			for (TaskModel t : pr.getTaskCol(s)) {
				JPanel tPanel = new JPanel();
				Color c = t.getCol();
				if (c == null)
					c = Color.LIGHT_GRAY;
				pane = BorderFactory.createMatteBorder(2, 2, 2, 2, c);
				tPanel.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue(), 125));
				tPanel.setOpaque(true);
				tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.PAGE_AXIS));
				tPanel.setBorder(new CompoundBorder(pane, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
				String title = "<HTML><U>" + t.getName() + "</U></HTML>";
				tPanel.add(new JLabel(title));
				tPanel.add(new JLabel(t.getDesc()));
				tPanel.add(new JLabel(t.dueToString()));
				col.add(tPanel);
			}
			this.add(col);
		}

	}

	private class ColView extends JPanel {
		private String title;

		public ColView(String t) {
			title = t;
			Border pane = BorderFactory.createTitledBorder(title);
			String blank = "";
			int i = 0;
			while (i < title.length()) {
				blank += "      ";
				i++;
			}
			this.add(new JLabel(blank));
			this.setBorder(pane);
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		}
	}

}
