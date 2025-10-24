package co.onclass.r2dbc;

import co.onclass.model.capacidad.Capacidad;
import co.onclass.model.capacidad.gateways.CapacidadRepository;
import co.onclass.r2dbc.entity.CapacidadEntity;
import co.onclass.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CapacidadReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Capacidad,
        CapacidadEntity,
        Long,
        CapacidadReactiveRepository
        > implements CapacidadRepository {
    public CapacidadReactiveRepositoryAdapter(CapacidadReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Capacidad.class));
    }

    @Override
    public Mono<Capacidad> guardarCapacidad(Capacidad capacidad) {
        return save(capacidad);
    }

    @Override
    public Mono<Capacidad> buscarPorId(Long idCapacidad) {
        return findById(idCapacidad);
    }
}
