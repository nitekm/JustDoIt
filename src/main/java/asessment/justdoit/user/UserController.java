package asessment.justdoit.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(
			summary = "Create a new user",
			description = "Creates a new user with the given details",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "User created successfully",
							content = @Content(schema = @Schema(implementation = UserDTO.class))
					)
			}
	)
	@PostMapping
	Mono<UserDTO> createUser(@RequestBody UserDTO user) {
		return userService.createUser(user);
	}

	@Operation(
			summary = "Get a user by ID",
			description = "Retrieves a user by their unique ID",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "User retrieved successfully",
							content = @Content(schema = @Schema(implementation = UserDTO.class))
					),
					@ApiResponse(
							responseCode = "404",
							description = "User not found",
							content = @Content
					)
			}
	)
	@GetMapping("/{id}")
	Mono<UserDTO> getUser(@PathVariable String id) {
		return userService.getUser(id);
	}
}
