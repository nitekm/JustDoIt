package asessment.justdoit.task;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

	Task() {}
	
	Task(String title, String description, TaskStatus status) {
		this.title = title;
		this.description = description;
		this.status = status;
	}

	String getId() {
		return id;
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

	TaskStatus getStatus() {
		return status;
	}

	void setStatus(TaskStatus status) {
		this.status = status;
	}

}
