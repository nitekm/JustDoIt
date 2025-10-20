package asessment.justdoit.exceptionhandling.exceptions;

public class TaskNotFound extends RuntimeException {
	public TaskNotFound(String id) {
		super("Task not found with id: " + id);
	}
}
