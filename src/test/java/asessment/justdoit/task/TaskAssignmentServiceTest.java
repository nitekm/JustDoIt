package asessment.justdoit.task;

import asessment.justdoit.exceptionhandling.exceptions.TaskNotFound;
import asessment.justdoit.user.UserAssignedTaskService;
import asessment.justdoit.user.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskAssignmentServiceTest {
	@Mock
	private TaskService taskService;

	@Mock
	private UserAssignedTaskService userAssignedTaskService;

	@InjectMocks
	private TaskAssignmentService taskAssignmentService;

	@Test
	void assignTasksToUser_shouldCallServicesForEachTask() {
		// Given
		String userId = "user1";
		Set<String> taskIds = Set.of("task1", "task2", "task3");
		final BulkAssignTaskRequest request = new BulkAssignTaskRequest(userId, taskIds);

		when(taskService.assignTaskToUser(anyString(), anyString()))
				.thenReturn(Mono.just(new TaskDTO("1", "not relevant", "", "", "", "")));
		when(userAssignedTaskService.assignTaskToUser(anyString(), anyString()))
				.thenReturn(Mono.just(new UserDTO("1", "not relevant", Set.of(""))));

		// When
		Mono<Void> result = taskAssignmentService.assignTasksToUser(request);

		// Then
		StepVerifier.create(result)
				.verifyComplete();

		// Verify interactions
		for (String taskId : taskIds) {
			verify(taskService).assignTaskToUser(taskId, userId);
			verify(userAssignedTaskService).assignTaskToUser(userId, taskId);
		}

		verifyNoMoreInteractions(taskService, userAssignedTaskService);
	}

	@Test
	void assignTasksToUser_shouldPropagateErrorIfTaskAssignmentFails() {
		// Given
		String userId = "user1";
		Set<String> taskIds = Set.of("task1");
		final BulkAssignTaskRequest request = new BulkAssignTaskRequest(userId, taskIds);

		when(taskService.assignTaskToUser(anyString(), anyString()))
				.thenReturn(Mono.error(new TaskNotFound("Task failure")));

		// When
		Mono<Void> result = taskAssignmentService.assignTasksToUser(request);

		// Then
		StepVerifier.create(result)
				.expectError(TaskNotFound.class)
				.verify();

		verify(taskService).assignTaskToUser("task1", userId);
		verify(userAssignedTaskService, never()).assignTaskToUser(anyString(), anyString());
	}
}