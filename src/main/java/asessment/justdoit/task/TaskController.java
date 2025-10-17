package asessment.justdoit.task;


import asessment.justdoit.exceptionhandling.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tasks")
class TaskController {

	private final TaskService taskService;
	private final TaskAssignmentService taskAssignmentService;

	TaskController(TaskService taskService, TaskAssignmentService taskAssignmentService) {
		this.taskService = taskService;
		this.taskAssignmentService = taskAssignmentService;
	}

	@Operation(
			summary = "Get all tasks",
			description = "Retrieves a list of all tasks in the system"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Successfully retrieved all tasks",
			content = @Content(schema = @Schema(implementation = TaskDTO.class))
	)
	@GetMapping
	Flux<TaskDTO> getAllTasks() {
		return taskService.getAllTasks();
	}


	@Operation(
			summary = "Get task by ID",
			description = "Retrieves a specific task by its unique identifier"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Task found",
					content = @Content(schema = @Schema(implementation = TaskDTO.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Task not found",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
			)
	})
	@GetMapping("/{id}")
	Mono<TaskDTO> getTaskById(
			@Parameter(description = "Task ID", required = true, example = "507f1f77bcf86cd799439011")
			@PathVariable String id
	) {
		return taskService.getTaskById(id);
	}

	@Operation(
			summary = "Create a new task",
			description = "Creates a new task with the provided information"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Task created successfully",
					content = @Content(schema = @Schema(implementation = TaskDTO.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Invalid task data",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
			)
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Mono<TaskDTO> createTask(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Task to create",
					required = true,
					content = @Content(schema = @Schema(implementation = TaskDTO.class))
			)
			@RequestBody TaskDTO taskDTO
	) {
		return taskService.save(taskDTO);
	}


	@Operation(
			summary = "Update a task",
			description = "Updates an existing task with the provided information"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Task updated successfully",
					content = @Content(schema = @Schema(implementation = TaskDTO.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Task not found",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Invalid task data",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
			)
	})
	@PutMapping("/{id}")
	Mono<TaskDTO> updateTask(
			@Parameter(description = "Task ID", required = true, example = "507f1f77bcf86cd799439011")
			@PathVariable String id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Updated task data",
					required = true,
					content = @Content(schema = @Schema(implementation = TaskDTO.class))
			)
			@RequestBody TaskDTO taskDTO
	) {
		return taskService.update(id, taskDTO);
	}


	@Operation(
			summary = "Delete a task",
			description = "Deletes a task by its unique identifier"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "204",
					description = "Task deleted successfully"
			),
			@ApiResponse(
					responseCode = "404",
					description = "Task not found",
					content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
			)
	})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	Mono<Void> deleteTask(
			@Parameter(description = "Task ID", required = true, example = "507f1f77bcf86cd799439011")
			@PathVariable String id
	) {
		return taskService.delete(id);
	}

	@Operation(
			summary = "Bulk assign tasks to user",
			description = "Assigns a list of tasks to a specific user"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "204",
					description = "Tasks assigned successfully"
			)
	})
	@PostMapping("/bulk-assign")
	Mono<Void> bulkAssignTasksToUser(@RequestBody BulkAssignTaskRequest request) {
		return taskAssignmentService.assignTasksToUser(request);
	}
}
