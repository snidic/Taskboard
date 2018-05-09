import java.util.Comparator;

public class TaskComparator implements Comparator<TaskModel> {

	@Override
	public int compare(TaskModel t1, TaskModel t2) {
		int comp = t1.getDue().compareTo(t2.getDue());
		if (comp != 0)
			return comp;
		comp = t1.getName().compareTo(t2.getName());
		return comp;
	}

}
