package co.onclass.r2dbc;

import co.onclass.r2dbc.entity.CapacidadBootcampEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CapacidadBootcampReactiveRepository extends ReactiveCrudRepository<CapacidadBootcampEntity, Long>,
        ReactiveQueryByExampleExecutor<CapacidadBootcampEntity> {
}
