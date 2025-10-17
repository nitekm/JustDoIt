package asessment.justdoit.task;

import asessment.justdoit.user.UserAssignedTaskService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
class TaskAssignmentService {

	private final TaskService taskService;
	private final UserAssignedTaskService userAssignedTaskService;

	TaskAssignmentService(TaskService taskService, UserAssignedTaskService userAssignedTaskService) {
		this.taskService = taskService;
		this.userAssignedTaskService = userAssignedTaskService;
	}

	Mono<Void> assignTasksToUser(BulkAssignTaskRequest request) {
		return Mono.fromFuture(() -> CompletableFuture.runAsync(() -> {
			request.taskIds().forEach(taskId -> {
				taskService.assignTaskToUser(taskId, request.assignedUserId()).block();
				userAssignedTaskService.assignTaskToUser(request.assignedUserId(), taskId).block();
			});
		}));
	}
}
