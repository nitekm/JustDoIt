package asessment.justdoit.exceptionhandling;

import asessment.justdoit.exceptionhandling.exceptions.InvalidTaskStatus;
import asessment.justdoit.exceptionhandling.exceptions.TaskNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(TaskNotFound.class)
	Mono<ResponseEntity<ApiErrorResponse>> handleTaskNotFound(TaskNotFound ex) {
		return Mono.just(buildErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(InvalidTaskStatus.class)
	Mono<ResponseEntity<ApiErrorResponse>> handleInvalidTaskStatus(InvalidTaskStatus ex) {
		return Mono.just(buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}

	private ResponseEntity<ApiErrorResponse> buildErrorResponse(int statusCode, String message) {
		return ResponseEntity.status(statusCode).body(new ApiErrorResponse(statusCode, message, LocalDateTime.now()));
	}
}
