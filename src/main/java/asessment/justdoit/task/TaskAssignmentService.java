package asessment.justdoit.task;

import asessment.justdoit.user.UserAssignedTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
class TaskAssignmentService {

	private static final Logger log = LoggerFactory.getLogger(TaskAssignmentService.class);
	private final TaskService taskService;
	private final UserAssignedTaskService userAssignedTaskService;

	TaskAssignmentService(TaskService taskService, UserAssignedTaskService userAssignedTaskService) {
		this.taskService = taskService;
		this.userAssignedTaskService = userAssignedTaskService;
	}

	Mono<Void> assignTasksToUser(BulkAssignTaskRequest request) {
		return Mono.fromFuture(CompletableFuture.completedFuture(request.taskIds()))
				.flatMap(taskIds ->
						Mono.when(
								taskIds.stream()
										.map(taskId ->
												taskService.assignTaskToUser(taskId, request.assignedUserId())
														.flatMap(success -> userAssignedTaskService.assignTaskToUser(request.assignedUserId(), taskId))
														.onErrorResume(e -> {
															log.error("Failed to assign task {}", taskId, e);
															return Mono.empty();
														})
										)
										.toList()
						)
				);
	}
}
