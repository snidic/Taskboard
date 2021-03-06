import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class TaskBoardModel extends JPanel {
	private List<ProjectModel> projects;
	private String fileName;
	private ProjectModel active;

	public TaskBoardModel() {
		projects = new ArrayList<ProjectModel>();
	}

	public TaskBoardModel(TaskBoardModel tb) {
		projects = tb.projects;
		fileName = tb.fileName;
		active = tb.active;
	}

	public void addProject(ProjectModel project) {
		if (projects.isEmpty())
			active = project;
		projects.add(project);

	}

	public ProjectModel getProject(String name) {
		for (ProjectModel p : projects)
			if (p.getName().equals(name))
				return p;
		return null;
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

	public List<ProjectModel> getProjects() {
		return projects;
	}

	public String getFileName() {
		return fileName;
	}

	public ProjectModel getActive() {
		return active;
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
		return this.getClass().getName() + ",\n\tProjects: " + projects.toString()
				+ ",\n\tFilename: " + fileName + ",\n\tActive: " + active + "]";
	}
}
