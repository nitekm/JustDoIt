package asessment.justdoit.user;

import asessment.justdoit.dto.UserDTO;
import asessment.justdoit.exceptionhandling.exceptions.UserNotFound;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class UserService {

	private final UserRepository userRepository;

	UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	Mono<UserDTO> createUser(UserDTO user) {
		return userRepository.save(new User(user.username()))
				.map(savedUser -> new UserDTO(
						savedUser.getId(),
						savedUser.getUsername(),
						savedUser.getTaskIds()
				));
	}

	Mono<UserDTO> getUser(String id) {
		return userRepository.findById(id)
				.switchIfEmpty(Mono.error(new UserNotFound(id)))
				.map(user -> new UserDTO(
						user.getId(),
						user.getUsername(),
						user.getTaskIds()
				));
	}

	Mono<UserDTO> updateUserWithAssignedTask(String userId, String taskId) {
		return userRepository.findById(userId)
				.switchIfEmpty(Mono.error(new UserNotFound(userId)))
				.flatMap(user -> {
					user.getTaskIds().add(taskId);
					return userRepository.save(user).map(savedUser -> new UserDTO(
							savedUser.getId(),
							savedUser.getUsername(),
							user.getTaskIds()
					));
				});
	}
}
