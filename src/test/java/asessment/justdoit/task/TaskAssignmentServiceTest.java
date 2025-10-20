package asessment.justdoit.task;

import asessment.justdoit.dto.TaskDTO;
import asessment.justdoit.exceptionhandling.exceptions.TaskNotFound;
import asessment.justdoit.user.UserAssignedTaskService;
import asessment.justdoit.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

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
				.thenReturn(Mono.just(new TaskDTO("1", "not relevant", "", "", "", "", 0L)));
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
	void assignTasksToUser_shouldContinueWhenTaskAssignmentFails() {
		// Given
		String userId = "user1";
		Set<String> taskIds = Set.of("task1", "task2");
		final BulkAssignTaskRequest request = new BulkAssignTaskRequest(userId, taskIds);

		when(taskService.assignTaskToUser(anyString(), anyString()))
				.thenAnswer(invocation -> {
					String taskId = invocation.getArgument(0);
					return "task1".equals(taskId)
							? Mono.error(new TaskNotFound(taskId))
							: Mono.empty();
				});

		// When
		Mono<Void> result = taskAssignmentService.assignTasksToUser(request);

		// Then
		StepVerifier.create(result).verifyComplete();
	}
}