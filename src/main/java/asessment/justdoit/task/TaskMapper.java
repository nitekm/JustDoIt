package asessment.justdoit.task;

import asessment.justdoit.dto.TaskDTO;

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
				task.getAssignedUserId(),
				task.getVersion()
		);
	}

	static Task toTask(TaskDTO dto) {
			return new Task(dto.title(), dto.description(), TaskStatus.getTaskStatus(dto.status()));
	}

	static Task toUpdatedTask(Task task, TaskDTO updatedTask) {
			return new Task(
					task.getId(),
					updatedTask.title(),
					updatedTask.description(),
					TaskStatus.getTaskStatus(updatedTask.status()),
					task.getCreationDate(),
					task.getAssignedUserId(),
					updatedTask.version()
			);
		}
}
