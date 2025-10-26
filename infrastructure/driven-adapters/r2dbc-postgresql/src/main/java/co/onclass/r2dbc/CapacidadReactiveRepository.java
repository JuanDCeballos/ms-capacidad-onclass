package co.onclass.r2dbc;

import co.onclass.r2dbc.entity.CapacidadEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CapacidadReactiveRepository extends ReactiveCrudRepository<CapacidadEntity, Long>,
        ReactiveQueryByExampleExecutor<CapacidadEntity> {

    @Query("SELECT COUNT(*) FROM capacidades")
    Mono<Long> countAll();
}
