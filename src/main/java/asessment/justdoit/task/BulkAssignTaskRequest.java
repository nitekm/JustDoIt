package asessment.justdoit.task;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Request user to bulk assign tasks to user")
record BulkAssignTaskRequest(

	@Schema(description = "ID of the user, who will be assigned to this task", example = "68f0c952b962ba21ce0ef308", required = true)
	String assignedUserId,

	@Schema(description = "IDs of tasks which will be assigned", example = "[1234, 4321]", required = true)
	Set<String> taskIds

) { }
