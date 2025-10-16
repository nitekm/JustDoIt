package asessment.justdoit.task;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
interface TaskRepository extends ReactiveMongoRepository<Task, String> {
	Flux<Task> findAll();
	Mono<Task> findById(String id);
	Mono<Task> save(Task task);
	Mono<Void> deleteById(String id);
}
