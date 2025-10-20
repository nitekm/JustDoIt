package asessment.justdoit.exceptionhandling.exceptions;

public class UserNotFound extends RuntimeException {
	public UserNotFound(String id) {
		super("User with id: " + id + " not found");
	}
}
