package asessment.justdoit.task;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Request user to bulk assign tasks to user")
record BulkAssignTaskRequest(

	@Schema(description = "ID of the user, who will be assigned to this task", example = "masterdev123", required = true)
	String assignedUserId,

	@Schema(description = "IDs of tasks which will be assigned")
	Set<String> taskIds

) { }
