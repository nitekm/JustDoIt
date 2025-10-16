package asessment.justdoit.exceptionhandling;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Error response")
public record ApiErrorResponse(
		@Schema(description = "HTTP status code", example = "404")
		int status,

		@Schema(description = "Error message", example = "Task not found with id: 507f1f77bcf86cd799439011")
		String message,

		@Schema(description = "Error timestamp", example = "2025-10-16T10:30:00")
		LocalDateTime timestamp
) { }
