package asessment.justdoit.task;

import asessment.justdoit.exceptionhandling.exceptions.InvalidTaskStatus;

enum TaskStatus {
	TODO, IN_PROGRESS, DONE;

	static TaskStatus getTaskStatus(String status) {
		if (status == null) return null;
		else {
			try {
				return TaskStatus.valueOf(status);
			} catch (IllegalArgumentException e) {
				throw new InvalidTaskStatus("Invalid task status: " + status);
			}
		}
	}
}
