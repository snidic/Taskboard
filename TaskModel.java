import java.util.Date;

public class TaskModel implements Comparable {
	private String name;
	private String desc;
	private String due;
	private String status;
	private static int instances = 1;

	public TaskModel(String name, String desc, String due, Object status) {
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
	}

	public TaskModel(TaskModel tm) {
		this.name = tm.name;
		this.desc = tm.desc;
		this.due = tm.due;
		this.status = tm.status;
	}

	public TaskModel() {
		this.name = "";
		this.desc = "";
		this.due = "";
		this.status = "";
	}

	@Override
	public int compareTo(Object o) {
		TaskModel other = (TaskModel) o;
		return due.compareTo(other.due);
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public String getDue() {
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

	public void setDue(String due) {
		this.due = due;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String toString() {
		return this.getClass().getName() + "[Name: " + name + " , Desc: " + desc + ", Due: " + due.toString()
				+ ", Status: " + status + "]";
	}

}