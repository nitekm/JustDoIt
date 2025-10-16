package asessment.justdoit.task;

import java.time.LocalDateTime;

public record TaskDTO(
		String title,
		String description,
		String creationDate,
		String status
) {
}
