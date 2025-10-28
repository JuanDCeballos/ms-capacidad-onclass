package co.onclass.r2dbc;

import co.onclass.model.capacidadbootcamp.CapacidadBootcamp;
import co.onclass.model.capacidadbootcamp.gateways.CapacidadBootcampRepository;
import co.onclass.r2dbc.entity.CapacidadBootcampEntity;
import co.onclass.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public class CapacidadBootcampReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        CapacidadBootcamp,
        CapacidadBootcampEntity,
        Long,
        CapacidadBootcampReactiveRepository
        > implements CapacidadBootcampRepository {
    public CapacidadBootcampReactiveRepositoryAdapter(CapacidadBootcampReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, CapacidadBootcamp.class));
    }

    @Override
    public Flux<Void> guardarCapacidadesBootcamp(List<CapacidadBootcamp> relaciones) {
        List<CapacidadBootcampEntity> entities = relaciones.stream()
                .map(rel -> mapper.map(rel, CapacidadBootcampEntity.class))
                .toList();

        return repository.saveAll(entities).then().flux();
    }
}
