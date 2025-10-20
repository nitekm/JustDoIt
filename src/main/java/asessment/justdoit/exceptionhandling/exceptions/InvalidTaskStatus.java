package asessment.justdoit.exceptionhandling.exceptions;

public class InvalidTaskStatus extends RuntimeException {
	public InvalidTaskStatus(String message) {
		super(message);
	}
}
