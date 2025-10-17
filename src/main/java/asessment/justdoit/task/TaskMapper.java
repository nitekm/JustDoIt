package asessment.justdoit.task;

import asessment.justdoit.dto.TaskDTO;
import asessment.justdoit.exceptionhandling.exceptions.InvalidTaskStatus;

import java.time.format.DateTimeFormatter;

final class TaskMapper {

	private TaskMapper() {}

	static TaskDTO toDTO(Task task) {
		final var formattedCreationDate = task.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return new TaskDTO(
				task.getId(),
				task.getTitle(),
				task.getDescription(),
				task.getStatus().name(),
				formattedCreationDate,
				task.getAssignedUserId()
		);
	}

	static Task toTask(TaskDTO dto) {
			return new Task(dto.title(), dto.description(), getTaskStatus(dto.status()));
	}

	static Task toUpdatedTask(Task task, TaskDTO updatedTask) {
			return new Task(
					task.getId(),
					updatedTask.title(),
					updatedTask.description(),
					getTaskStatus(updatedTask.status()),
					task.getCreationDate(),
					task.getAssignedUserId()
			);
		}

	private static TaskStatus getTaskStatus(String status) {
		if (status == null) return null;
		else {
			try {
				return TaskStatus.valueOf(status);
			} catch (IllegalArgumentException e) {
				throw new InvalidTaskStatus("Invalid task status: " + status);
			}
		}
	}
}
