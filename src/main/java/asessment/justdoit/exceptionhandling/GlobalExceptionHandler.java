package asessment.justdoit.exceptionhandling;

import asessment.justdoit.exceptionhandling.exceptions.InvalidTaskStatus;
import asessment.justdoit.exceptionhandling.exceptions.TaskNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
class GlobalExceptionHandler {

	@ExceptionHandler(TaskNotFound.class)
	Mono<ResponseEntity<ErrorResponse>> handleTaskNotFound(TaskNotFound ex) {
		final var error = buildErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return Mono.just(ResponseEntity.badRequest().body(error));
	}

	@ExceptionHandler(InvalidTaskStatus.class)
	Mono<ResponseEntity<ErrorResponse>> handleInvalidTaskStatus(InvalidTaskStatus ex) {
		final var error = buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return Mono.just(ResponseEntity.badRequest().body(error));
	}

	private ErrorResponse buildErrorResponse(int statusCode, String message) {
		return new ErrorResponse(statusCode, message, LocalDateTime.now());
	}
}
