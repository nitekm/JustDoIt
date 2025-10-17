package asessment.justdoit.task;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Task data transfer object")
record TaskDTO(
		@Schema(description = "Task unique identifier", example = "507f1f77bcf86cd799439011", accessMode = Schema.AccessMode.READ_ONLY)
		String id,

		@Schema(description = "Task title", example = "Complete project documentation", required = true)
		String title,

		@Schema(description = "Task description", example = "Write comprehensive API documentation with examples")
		String description,

		@Schema(description = "Task status", example = "IN_PROGRESS", allowableValues = {"TODO", "IN_PROGRESS", "DONE"}, required = true)
		String status,

		@Schema(description = "Task creation date", example = "2025-10-16T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
		String creationDate,

		@Schema(description = "ID of the user, who is assigned to this task", example = "masterdev123")
		String assignedUserId
) {}
