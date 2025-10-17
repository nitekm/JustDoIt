package asessment.justdoit.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Document(collection = "users")
class User {
	@Id
	private String id;
	private String username;
	private Set<String> taskIds = Collections.emptySet();

	User() {}

	User(String username) {
		this.username = username;
	}

	String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	String getUsername() {
		return username;
	}

	void setUsername(String username) {
		this.username = username;
	}

	Set<String> getTaskIds() {
		return taskIds;
	}

	void setTaskIds(Set<String> taskIds) {
		this.taskIds = taskIds;
	}
}
