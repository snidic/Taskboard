import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectModel {
	private String name;
	private List<String> cols;
	private List<TaskModel> tasks;
	private static int instances = 1;
	private static Comparator comp = new TaskComparator();

	public ProjectModel(String name, ArrayList<String> cols) {
		if ("".equals(name))
			this.name = "project" + instances++;
		else
			this.name = name;
		this.cols = new ArrayList<String>(cols);
		this.tasks = new ArrayList<TaskModel>();
	}

	public ProjectModel(String name, ArrayList<String> cols, List<TaskModel> tasks) {
		if ("".equals(name))
			this.name = "project" + instances++;
		else
			this.name = name;
		this.cols = new ArrayList<String>(cols);
		this.tasks = new ArrayList<TaskModel>(tasks);
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

	// TODO: SORT
	public void addTask(TaskModel task) {
		tasks.add(task);
		Collections.sort(tasks, comp);
	}

	public void addColumn(String col) {
		if (cols.contains(col))
			return;
		cols.add(col);
	}

	public List<TaskModel> getTaskCol(String col) {
		ArrayList<TaskModel> list = new ArrayList<>();
		for (TaskModel t : tasks)
			if (t.getStatus().equals(col))
				list.add(t);
		return list;
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

	public void setAs(ProjectModel pm) {
		this.name = pm.name;
		this.cols = pm.cols;
		this.tasks = pm.tasks;
	}

	public String toString() {
		return this.getClass().getName() + " - " + name + " : " + cols.size() + " - "
				+ tasks.size();
	}
}
