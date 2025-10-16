package asessment.justdoit.task;

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
				formattedCreationDate,
				task.getStatus().name()
		);
	}

	static Task toTask(TaskDTO dto) {
		TaskStatus status;
		try {
			status = TaskStatus.valueOf(dto.status());
		} catch (IllegalArgumentException e) {
			throw new InvalidTaskStatus("Invalid task status: " + dto.status());
		}
		return new Task(dto.title(), dto.description(), status);
	}
}
