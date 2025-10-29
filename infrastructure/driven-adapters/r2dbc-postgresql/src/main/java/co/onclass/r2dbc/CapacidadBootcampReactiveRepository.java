package co.onclass.r2dbc;

import co.onclass.r2dbc.entity.CapacidadBootcampEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CapacidadBootcampReactiveRepository extends ReactiveCrudRepository<CapacidadBootcampEntity, Long>,
        ReactiveQueryByExampleExecutor<CapacidadBootcampEntity> {

    Flux<CapacidadBootcampEntity> findAllByIdBootcampIn(List<Long> ids);
}
