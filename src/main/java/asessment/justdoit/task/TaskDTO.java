package asessment.justdoit.task;

public record TaskDTO(
		String id,
		String title,
		String description,
		String creationDate,
		String status
) {
}
