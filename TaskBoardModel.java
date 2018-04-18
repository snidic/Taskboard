import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class TaskBoardModel extends JPanel {
	private String name;
	private List<ProjectModel> projects;
	private String fileName;
	private ProjectModel active;

	public TaskBoardModel() {
		this.name = "";
		projects = new ArrayList<ProjectModel>();
		fileName = "";
	}

	public TaskBoardModel(TaskBoardModel tb) {
		this.name = tb.name;
		projects = tb.projects;
		fileName = tb.fileName;
		active = tb.active;
	}

	public TaskBoardModel(ArrayList<Object> list) {
		int i = 0;
		name = (String) list.get(i++);
		projects = (List<ProjectModel>) list.get(i++);
		fileName = (String) list.get(i++);
		active = (ProjectModel) list.get(i++);

	}

	public void addProject(ProjectModel project) {
		if (projects.isEmpty())
			active = project;
		projects.add(project);

	}

	public void addTaskToActive(TaskModel task) {
		active.addTask(task);
	}

	public boolean containsProjectName(String other) {
		for (ProjectModel p : projects)
			if (p.getName().equals(other))
				return true;
		return false;
	}

	public String getName() {
		return name;
	}

	public List<ProjectModel> getProjects() {
		return projects;
	}

	public String getFileName() {
		return fileName;
	}

	public ProjectModel getActive() {
		return active;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProjects(List<ProjectModel> projects) {
		this.projects = projects;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setActive(ProjectModel active) {
		this.active = active;
	}

	public String toString() {
		return this.getClass().getName() + "[Name: " + name + ", Projects: " + projects.toString() + ", Filename: "
				+ fileName + ", Active: " + active + "]";
	}
}
