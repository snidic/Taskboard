import java.awt.Color;
import java.util.Date;

public class TaskModel {
	private String name;
	private String desc;
	private Date due;
	private String status;
	private Color col;
	private static int instances = 1;

	public TaskModel(String name, String desc, Date due, Object status, Color col) {
		if ("".equals(name))
			this.name = "task" + instances++;
		else
			this.name = name;
		this.desc = desc;
		this.due = due;
		if (status instanceof String)
			this.status = (String) status;
		else
			this.status = "";
		this.col = col;
	}

	public TaskModel(TaskModel tm) {
		this.name = tm.name;
		this.desc = tm.desc;
		this.due = tm.due;
		this.status = tm.status;
		this.col = tm.col;
	}

	public TaskModel() {
		this.name = "";
		this.desc = "";
		this.due = null;
		this.status = "";
		this.col = null;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public Date getDue() {
		return due;
	}

	public String getStatus() {
		return status;
	}

	public static int getInstances() {
		return instances;
	}

	public static void setInstances(int instances) {
		TaskModel.instances = instances;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setDue(Date due) {
		this.due = due;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Color getCol() {
		return col;
	}

	public void setCol(Color col) {
		this.col = col;
	}

	public void setAs(TaskModel tm) {
		this.name = tm.name;
		this.desc = tm.desc;
		this.due = tm.due;
		this.status = tm.status;
		this.col = tm.col;
	}

	public static String dueToString(Date d) {
		String str = "";
		if (d == null || d.equals(CalendarView.ENDLESS))
			return str;
		String[] contents = d.toString().split(" ");
		return contents[2] + "/" + contents[1] + "/" + contents[contents.length - 1];
	}

	public String dueToString() {
		String str = "";
		if (due == null || due.equals(CalendarView.ENDLESS))
			return str;
		String[] contents = due.toString().split(" ");
		return contents[2] + "/" + contents[1] + "/" + contents[contents.length - 1];
	}

	public String toString() {
		return name + " : " + status + " :: " + dueToString();
	}

}
