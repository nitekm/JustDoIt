package asessment.justdoit.exceptionhandling;

import asessment.justdoit.exceptionhandling.exceptions.InvalidTaskStatus;
import asessment.justdoit.exceptionhandling.exceptions.TaskNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
class GlobalExceptionHandler {

	@ExceptionHandler(TaskNotFound.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	Mono<ApiErrorResponse> handleTaskNotFound(TaskNotFound ex) {
		return Mono.just(buildErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(InvalidTaskStatus.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	Mono<ApiErrorResponse> handleInvalidTaskStatus(InvalidTaskStatus ex) {
		return Mono.just(buildErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}

	private ApiErrorResponse buildErrorResponse(int statusCode, String message) {
		return new ApiErrorResponse(statusCode, message, LocalDateTime.now());
	}
}
