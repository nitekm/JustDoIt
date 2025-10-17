package asessment.justdoit.task;

import asessment.justdoit.dto.TaskDTO;
import asessment.justdoit.exceptionhandling.ApiErrorResponse;
import asessment.justdoit.exceptionhandling.GlobalExceptionHandler;
import asessment.justdoit.exceptionhandling.exceptions.InvalidTaskStatus;
import asessment.justdoit.exceptionhandling.exceptions.TaskNotFound;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

	@Mock
	private TaskService taskService;

	@InjectMocks
	private TaskController taskController;

	private WebTestClient webTestClient;

	@BeforeEach
	void setUp() {
		webTestClient = WebTestClient
				.bindToController(taskController)
				.controllerAdvice(GlobalExceptionHandler.class)
				.build();
	}

	@Test
	@DisplayName("Should return all tasks")
	void getAllTasks_ok() {
		TaskDTO task1 = createTaskDTO("1", "Task 1");
		TaskDTO task2 = createTaskDTO("2", "Task 2");

		when(taskService.getAllTasks()).thenReturn(Flux.just(task1, task2));

		webTestClient.get()
				.uri("/tasks")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(TaskDTO.class)
				.hasSize(2)
				.contains(task1, task2);
	}

	@Test
	@DisplayName("Should return task by ID")
	void getTaskByIdWhenTaskExistsReturnsTask() {
		String taskId = "1";
		TaskDTO task = createTaskDTO(taskId, "Task 1");

		when(taskService.getTaskById(taskId)).thenReturn(Mono.just(task));

		webTestClient.get()
				.uri("/tasks/{id}", taskId)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(TaskDTO.class)
				.isEqualTo(task);
	}

	@Test
	@DisplayName("Should return 404 when task not found")
	void getTaskByIdWhenTaskNotFoundReturns404() {
		String taskId = "nonexistent";

		when(taskService.getTaskById(taskId)).thenReturn(Mono.error(new TaskNotFound(taskId)));

		webTestClient.get()
				.uri("/tasks/{id}", taskId)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	@DisplayName("Should create new task")
	void createsTask() {
		TaskDTO task = createTaskDTO(null, "New Task");
		TaskDTO savedTask = createTaskDTO("1", "New task");

		when(taskService.save(task)).thenReturn(Mono.just(savedTask));

		webTestClient.post()
				.uri("/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(task)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(TaskDTO.class)
				.isEqualTo(savedTask);
	}

	@Test
	@DisplayName("Should return 400 when task to create has invalid status")
	void createTaskWhenInvalidStatusReturnsBadRequest() {
		TaskDTO invalidTask = createTaskDTO(null, "Task with invalid status");

		when(taskService.save(invalidTask))
				.thenReturn(Mono.error(new InvalidTaskStatus("Invalid task status")));

		webTestClient.post()
				.uri("/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(invalidTask)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(ApiErrorResponse.class);
	}

	@Test
	@DisplayName("Should update existing task")
	void updateTaskReturnsUpdatedTask() {
		String taskId = "1";
		TaskDTO updatedTask = createTaskDTO(taskId, "Updated Task");

		when(taskService.update(taskId, updatedTask)).thenReturn(Mono.just(updatedTask));

		webTestClient.put()
				.uri("/tasks/{id}", taskId)
						.bodyValue(updatedTask)
						.exchange()
						.expectStatus().isOk()
						.expectBody(TaskDTO.class)
						.isEqualTo(updatedTask);
	}

	@Test
	@DisplayName("Should return 400 when task to update has invalid status")
	void updateTaskWhenInvalidStatusReturnsBadRequest() {
		String taskId = "1";
		TaskDTO invalidTask = createTaskDTO(taskId, "Updated task with invalid status");

		when(taskService.update(taskId, invalidTask))
				.thenReturn(Mono.error(new InvalidTaskStatus("Invalid task status")));

		webTestClient.put()
				.uri("/tasks/{id}", taskId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(invalidTask)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(ApiErrorResponse.class);
	}

	@Test
	@DisplayName("Should return 409 when task to update has outdated version")
	void updateTaskWithOutdatedVersionReturnsConflict() {
		String taskId = "1";
		TaskDTO invalidTask = createTaskDTO(taskId, "Updated task with old version");

		when(taskService.update(taskId, invalidTask))
				.thenReturn(Mono.error(new OptimisticLockingFailureException("Version conflict")));

		webTestClient.put()
				.uri("/tasks/{id}", taskId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(invalidTask)
				.exchange()
				.expectStatus().is4xxClientError()
				.expectBody(ApiErrorResponse.class);
	}

	@Test
	@DisplayName("Should delete existing task")
	void deleteTaskReturnsNoContent() {
		String taskId = "1";

		when(taskService.delete(taskId)).thenReturn(Mono.empty());

		webTestClient.delete()
				.uri("/tasks/{id}", taskId)
				.exchange()
				.expectStatus().isNoContent();
	}

	@Test
	@DisplayName("Should return 404 when trying to delete nonexistent task")
	void deleteTaskWhenNonexistentTaskReturnsNotFound() {
		String taskId = "999";

		when(taskService.delete(taskId)).thenReturn(Mono.error(new TaskNotFound(taskId)));

		webTestClient.delete()
				.uri("/tasks/{id}", taskId)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(ApiErrorResponse.class);
	}

	private TaskDTO createTaskDTO(String id, String title) {
		return new TaskDTO(
				id,
				title,
				"test task",
				TaskStatus.TODO.name(),
				null,
				null,
				0L
		);
	}

}