package asessment.justdoit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Schema(description = "User data transfer object")
public record UserDTO(
		@Schema(description = "User unique identifier", example = "507f1f77bcf86cd799439011", accessMode = Schema.AccessMode.READ_ONLY)
		String id,
		@Schema(description = "User username", example = "masterdev123", required = true)
		@NotEmpty(message = "Username must not be empty")
		String username,
		@Schema(description = "IDs of tasks assigned to this user", example = "[1234, 4321]", accessMode = Schema.AccessMode.READ_ONLY)
		Set<String> assignedTaskIds
) { }
