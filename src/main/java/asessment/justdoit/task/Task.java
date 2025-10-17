package asessment.justdoit.task;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "tasks")
class Task {

	@Id
	private String id;

	private String title;
	private String description;

	@CreatedDate
	private LocalDateTime creationDate;

	private TaskStatus status;

	private String assignedUserId;

	@Version
	private Long version;

	Task() {}

	Task(String id, String title, String description, TaskStatus status, LocalDateTime creationDate, String assignedUserId, Long version) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.status = status;
		this.creationDate = creationDate;
		this.assignedUserId = assignedUserId;
		this.version = version;
	}

	Task(String title, String description, TaskStatus status) {
		this.title = title;
		this.description = description;
		this.status = status;
	}

	String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	String getTitle() {
		return title;
	}

	void setTitle(String title) {
		this.title = title;
	}

	String getDescription() {
		return description;
	}

	void setDescription(String description) {
		this.description = description;
	}

	LocalDateTime getCreationDate() {
		return creationDate;
	}

	void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	TaskStatus getStatus() {
		return status;
	}

	void setStatus(TaskStatus status) {
		this.status = status;
	}

	String getAssignedUserId() {
		return assignedUserId;
	}

	void setAssignedUserId(String assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	Long getVersion() {
		return version;
	}
}
