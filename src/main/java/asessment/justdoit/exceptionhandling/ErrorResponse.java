package asessment.justdoit.exceptionhandling;

import java.time.LocalDateTime;

record ErrorResponse(
		int statusCode,
		String message,
		LocalDateTime timestamp
) {
}
