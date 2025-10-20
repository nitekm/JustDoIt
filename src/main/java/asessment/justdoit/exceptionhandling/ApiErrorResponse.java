package asessment.justdoit.exceptionhandling;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Error response")
public record ApiErrorResponse(
		@Schema(description = "HTTP error status code")
		int status,

		@Schema(description = "Error message", example = "An error message describing what went wrong")
		String message,

		@Schema(description = "Error timestamp", example = "2025-10-16T10:30:00")
		LocalDateTime timestamp
) { }
