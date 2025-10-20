package asessment.justdoit.user;

import asessment.justdoit.dto.UserDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserAssignedTaskService {

	private final UserService userService;

	UserAssignedTaskService(UserService userService) {
		this.userService = userService;
	}

	public Mono<UserDTO> assignTaskToUser(String userId, String taskId) {
		return userService.updateUserWithAssignedTask(userId, taskId);
	}

}
