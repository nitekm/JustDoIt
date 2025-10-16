package asessment.justdoit.task;

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
				.map(existingTask -> updateTask(existingTask, updatedTask))
				.flatMap(taskRepository::save)
				.map(TaskMapper::toDTO);
	}

	Mono<Void> delete(String id) {
		return taskRepository.deleteById(id);
	}

	Mono<TaskDTO> getTaskById(String id) {
		return taskRepository.findById(id)
				.map(TaskMapper::toDTO);
	}


	Flux<TaskDTO> getAllTasks() {
		return taskRepository.findAll()
				.map(TaskMapper::toDTO);
	}

	private Task updateTask(Task existingTask, TaskDTO updatedTask) {
		existingTask.setTitle(updatedTask.title());
		existingTask.setDescription(updatedTask.description());
		existingTask.setStatus(TaskStatus.valueOf(updatedTask.status()));
		return existingTask;
	}

}
