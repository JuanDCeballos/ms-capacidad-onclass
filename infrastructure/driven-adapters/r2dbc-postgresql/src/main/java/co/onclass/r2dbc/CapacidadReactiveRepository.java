package co.onclass.r2dbc;

import co.onclass.r2dbc.entity.CapacidadEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CapacidadReactiveRepository extends ReactiveCrudRepository<CapacidadEntity, Long>,
        ReactiveQueryByExampleExecutor<CapacidadEntity> {

}
