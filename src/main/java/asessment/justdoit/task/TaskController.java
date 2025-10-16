package asessment.justdoit.task;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tasks")
class TaskController {

	private final TaskService taskService;

	TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping
	Flux<TaskDTO> getAllTasks() {
		return taskService.getAllTasks();
	}

	@GetMapping("/{id}")
	Mono<TaskDTO> getTaskById(@PathVariable String id) {
		return taskService.getTaskById(id);
	}

	@PostMapping
	Mono<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
		return taskService.save(taskDTO);
	}

	@PutMapping("/{id}")
	Mono<TaskDTO> updateTask(@PathVariable String id, @RequestBody TaskDTO taskDTO) {
		return taskService.update(id, taskDTO);
	}

	@DeleteMapping("{id}")
	Mono<Void> deleteTask(@PathVariable String id) {
		return taskService.delete(id);
	}
}
