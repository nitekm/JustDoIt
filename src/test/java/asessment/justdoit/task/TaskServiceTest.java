package asessment.justdoit.task;

import asessment.justdoit.exceptionhandling.exceptions.InvalidTaskStatus;
import asessment.justdoit.exceptionhandling.exceptions.TaskNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskService taskService;

	private Task testTask;

	@BeforeEach
	void setUp() {
		testTask = new Task("Test Task", "Test Description", TaskStatus.TODO);
		testTask.setId("67309a1b2c3d4e5f6a7b8c9d");
		testTask.setCreationDate(LocalDateTime.of(2025, 1, 1, 0, 0));
	}

	@Test
	@DisplayName("Should throw InvalidTaskStatus when saving task with invalid status")
	void shouldThrowExceptionWhenSavingTaskWithInvalidStatus() {
		// Given
		TaskDTO invalidDTO = createTaskDTO("Invalid Task", "INVALID_STATUS");

		// When, then
		assertThrows(InvalidTaskStatus.class, () -> taskService.save(invalidDTO));

		verify(taskRepository, never()).save(any(Task.class));
	}

	@Test
	@DisplayName("Should save task successfully")
	void shouldSaveTaskSuccessfully() {
		// Given
		Task savedTask = createTask("New Task");
		mockSaveReturns(savedTask);
		TaskDTO receivedTaskDTO = createTaskDTO("New Task", TaskStatus.TODO.name());

		// When
		Mono<TaskDTO> result = taskService.save(receivedTaskDTO);

		// Then
		StepVerifier.create(result)
				.assertNext(taskDTO -> {
					assertThat(taskDTO).isNotNull();
					assertThat(taskDTO.title()).isEqualTo("New Task");
				})
				.verifyComplete();

		verify(taskRepository, times(1)).save(any(Task.class));
	}

	@Test
	@DisplayName("Should get task by id successfully")
	void shouldGetTaskByIdSuccessfully() {
		// Given
		mockFindByIdReturns(testTask);

		// When
		Mono<TaskDTO> result = taskService.getTaskById(testTask.getId());

		// Then
		StepVerifier.create(result)
				.assertNext(taskDTO -> {
					assertThat(taskDTO).isNotNull();
					assertThat(taskDTO.title()).isEqualTo("Test Task");
				})
				.verifyComplete();

		verify(taskRepository, times(1)).findById(testTask.getId());
	}

	@Test
	@DisplayName("Should throw TaskNotFound when task does not exist")
	void shouldThrowTaskNotFoundWhenTaskDoesNotExist() {
		// Given
		String taskId = "nonexistent";
		when(taskRepository.findById(taskId)).thenReturn(Mono.empty());

		// When
		Mono<TaskDTO> result = taskService.getTaskById(taskId);

		// Then
		StepVerifier.create(result)
				.expectError(TaskNotFound.class)
				.verify();

		verify(taskRepository, times(1)).findById(taskId);
	}

	@Test
	@DisplayName("Should get all tasks successfully")
	void shouldGetAllTasksSuccessfully() {
		// Given
		Task task1 = createTask("Task 1");
		Task task2 = createTask("Task 2");
		Task task3 = createTask("Task 3");

		when(taskRepository.findAll()).thenReturn(Flux.just(task1, task2, task3));

		// When
		Flux<TaskDTO> result = taskService.getAllTasks();

		// Then
		StepVerifier.create(result)
				.assertNext(taskDTO -> assertThat(taskDTO.title()).isEqualTo("Task 1"))
				.assertNext(taskDTO -> assertThat(taskDTO.title()).isEqualTo("Task 2"))
				.assertNext(taskDTO -> assertThat(taskDTO.title()).isEqualTo("Task 3"))
				.verifyComplete();

		verify(taskRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("Should return empty flux when no tasks exist")
	void shouldReturnEmptyFluxWhenNoTasksExist() {
		// Given
		when(taskRepository.findAll()).thenReturn(Flux.empty());

		// When
		Flux<TaskDTO> result = taskService.getAllTasks();

		// Then
		StepVerifier.create(result)
				.verifyComplete();

		verify(taskRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("Should update task successfully")
	void shouldUpdateTaskSuccessfully() {
		// Given
		TaskDTO updatedTaskDTO = new TaskDTO(
				testTask.getId(),
				"Updated Title",
				"Updated Description",
				TaskStatus.IN_PROGRESS.name(),
				"2025-10-10T15:00:00",
				null
		);

		when(taskRepository.findById(testTask.getId())).thenReturn(Mono.just(testTask));
		when(taskRepository.save(any(Task.class))).thenReturn(Mono.just(testTask));

		// When
		Mono<TaskDTO> result = taskService.update(testTask.getId(), updatedTaskDTO);

		// Then
		StepVerifier.create(result)
				.assertNext(taskDTO -> {
					assertThat(taskDTO).isNotNull();
					assertThat(taskDTO.title()).isEqualTo("Updated Title");
					assertThat(taskDTO.description()).isEqualTo("Updated Description");
					assertThat(taskDTO.status()).isEqualTo("IN_PROGRESS");
				})
				.verifyComplete();

		verify(taskRepository, times(1)).findById(testTask.getId());
		verify(taskRepository, times(1)).save(any(Task.class));
	}

	@Test
	@DisplayName("Should throw TaskNotFound when updating non-existent task")
	void shouldThrowTaskNotFoundWhenUpdatingNonExistentTask() {
		// Given
		String taskId = "nonexistent";
		TaskDTO updatedTaskDTO = new TaskDTO(taskId, "Title", "Desc", "TODO", null, null);

		when(taskRepository.findById(taskId)).thenReturn(Mono.empty());

		// When
		Mono<TaskDTO> result = taskService.update(taskId, updatedTaskDTO);

		// Then
		StepVerifier.create(result)
				.expectError(TaskNotFound.class)
				.verify();

		verify(taskRepository, times(1)).findById(taskId);
		verify(taskRepository, never()).save(any(Task.class));
	}

	@Test
	@DisplayName("Should delete task successfully")
	void shouldDeleteTaskSuccessfully() {
		// Given
		mockFindByIdReturns(testTask);
		when(taskRepository.deleteById(testTask.getId())).thenReturn(Mono.empty());

		// When
		Mono<Void> result = taskService.delete(testTask.getId());

		// Then
		StepVerifier.create(result)
				.verifyComplete();

		verify(taskRepository, times(1)).findById(testTask.getId());
		verify(taskRepository, times(1)).deleteById(testTask.getId());
	}

	@Test
	@DisplayName("Should throw TaskNotFound when deleting non-existent task")
	void shouldThrowTaskNotFoundWhenDeletingNonExistentTask() {
		// Given
		String taskId = "nonexistent";
		when(taskRepository.findById(taskId)).thenReturn(Mono.empty());

		// When
		Mono<Void> result = taskService.delete(taskId);

		// Then
		StepVerifier.create(result)
				.expectError(TaskNotFound.class)
				.verify();

		verify(taskRepository, times(1)).findById(taskId);
		verify(taskRepository, never()).deleteById(anyString());
	}

	private Task createTask(String title) {
		Task task = new Task();
		task.setId("1234567890");
		task.setTitle(title);
		task.setDescription("Test Description");
		task.setStatus(TaskStatus.TODO);
		task.setCreationDate(LocalDateTime.now());
		return task;
	}

	private TaskDTO createTaskDTO(String title, String status) {
		return new TaskDTO(
				null,
				title,
				"Test Description",
				status,
				null,
				null
		);
	}

	private void mockFindByIdReturns(Task task) {
		when(taskRepository.findById(task.getId())).thenReturn(Mono.just(task));
	}

	private void mockSaveReturns(Task task) {
		when(taskRepository.save(any(Task.class))).thenReturn(Mono.just(task));
	}
}