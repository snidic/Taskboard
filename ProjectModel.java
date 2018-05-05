import java.util.ArrayList;
import java.util.List;

public class ProjectModel {
	private String name;
	private List<String> cols;
	private List<TaskModel> tasks;
	private static int instances = 1;

	public ProjectModel(String name, ArrayList<String> cols) {
		if ("".equals(name))
			this.name = "project" + instances+1;
		else
			this.name = name;
		this.cols = new ArrayList<String>(cols);
		this.tasks = new ArrayList<TaskModel>();
		instances++;
	}

	public ProjectModel(ProjectModel pm) {
		this.name = pm.name;
		this.cols = pm.cols;
		this.tasks = pm.tasks;
	}

	public ProjectModel() {
		this.name = "";
		this.cols = new ArrayList<String>();
		this.tasks = new ArrayList<TaskModel>();
	}

	public void addTask(TaskModel task) {
		tasks.add(task);
	}

	public void addColumn(String col) {
		if (cols.contains(col))
			return;
		cols.add(col);
	}

	public String getName() {
		return name;
	}

	public List<String> getCols() {
		return cols;
	}

	public List<TaskModel> getTasks() {
		return tasks;
	}

	public static int getInstances() {
		return instances;
	}

	public static void setInstances(int instances) {
		ProjectModel.instances = instances;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCols(List<String> cols) {
		this.cols = cols;
	}

	public void setTasks(List<TaskModel> tasks) {
		this.tasks = tasks;
	}

	public String toString() {
		return this.getClass().getName() + "[Name: " + name + ",\n\tCols: " + cols.toString() + ",\n\tTasks:"
				+ tasks.toString() + "]";
	}
}
