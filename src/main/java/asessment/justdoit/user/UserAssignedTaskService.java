package asessment.justdoit.user;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class UserAssignedTaskService {

	private final UserService userService;

	UserAssignedTaskService(UserService userService) {
		this.userService = userService;
	}

	public Mono<UserDTO> assignTaskToUser(String userId, String taskId) {
		return userService.updateUserWithAssignedTasks(userId, Collections.singleton(taskId));
	}
}
