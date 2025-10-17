package asessment.justdoit.task;

import asessment.justdoit.dto.TaskDTO;
import asessment.justdoit.exceptionhandling.exceptions.TaskNotFound;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
class TaskService {

	private final TaskRepository taskRepository;

	TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	Mono<TaskDTO> save(TaskDTO task) {
		return taskRepository.save(TaskMapper.toTask(task))
				.map(TaskMapper::toDTO);
	}

	Mono<TaskDTO> update(String taskId, TaskDTO updatedTask) {
		return taskRepository.findById(taskId)
				.switchIfEmpty(Mono.error(new TaskNotFound(taskId)))
				.map(existingTask -> TaskMapper.toUpdatedTask(existingTask, updatedTask))
				.flatMap(taskRepository::save)
				.map(TaskMapper::toDTO);
	}

	Mono<Void> delete(String id) {
		return taskRepository.findById(id)
				.switchIfEmpty(Mono.error(new TaskNotFound(id)))
				.flatMap(task -> taskRepository.deleteById(task.getId()));
	}

	Mono<TaskDTO> getTaskById(String id) {
		return taskRepository.findById(id)
				.switchIfEmpty(Mono.error(new TaskNotFound(id)))
				.map(TaskMapper::toDTO);
	}


	Flux<TaskDTO> getAllTasks() {
		return taskRepository.findAll()
				.map(TaskMapper::toDTO);
	}

	Mono<TaskDTO> assignTaskToUser(String taskId, String userId) {
		return taskRepository.findById(taskId)
				.switchIfEmpty(Mono.error(new TaskNotFound(taskId)))
				.flatMap(task -> {
					task.setAssignedUserId(userId);
					return taskRepository.save(task).map(TaskMapper::toDTO);
				});
	}
}
