package co.onclass.r2dbc;

import co.onclass.model.capacidad.Capacidad;
import co.onclass.model.capacidad.gateways.CapacidadRepository;
import co.onclass.model.paging.PageableQuery;
import co.onclass.model.paging.SortDirection;
import co.onclass.r2dbc.entity.CapacidadEntity;
import co.onclass.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class CapacidadReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Capacidad,
        CapacidadEntity,
        Long,
        CapacidadReactiveRepository
        > implements CapacidadRepository {

    private final R2dbcEntityTemplate template;

    public CapacidadReactiveRepositoryAdapter(CapacidadReactiveRepository repository, ObjectMapper mapper,
                                              R2dbcEntityTemplate template) {
        super(repository, mapper, d -> mapper.map(d, Capacidad.class));
        this.template = template;
    }

    @Override
    public Mono<Capacidad> guardarCapacidad(Capacidad capacidad) {
        return save(capacidad);
    }

    @Override
    public Mono<Capacidad> buscarPorId(Long idCapacidad) {
        return findById(idCapacidad);
    }

    @Override
    public Mono<Long> contarTodos() {
        return repository.countAll();
    }

    @Override
    public Flux<Capacidad> buscarTodasPaginadas(PageableQuery query) {
        Sort.Direction springDirection = query.getDirection() == SortDirection.DESC
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable springPageable = PageRequest.of(
                query.getPage(),
                query.getSize(),
                Sort.by(springDirection, query.getSortBy())
        );

        Query paginatedQuery = Query.empty().with(springPageable);

        return template.select(CapacidadEntity.class)
                .matching(paginatedQuery)
                .all()
                .map(this::toEntity);
    }

    @Override
    public Flux<Capacidad> buscarTodasPorIdEnOrden(List<Long> ids) {
        return repository.findAllById(ids)
                .map(this::toEntity)
                .collectList()
                .flatMapMany(capacidads -> {
                    Map<Long, Capacidad> mapa = capacidads.stream()
                            .collect(Collectors.toMap(Capacidad::getId, Function.identity()));

                    return Flux.fromIterable(ids)
                            .map(mapa::get)
                            .filter(Objects::nonNull);
                });
    }

    @Override
    public Mono<Long> contarCapacidadesExistentes(List<Long> ids) {
        return repository.countByIdIn(ids);
    }

    @Override
    public Flux<Capacidad> buscarTodasPorId(List<Long> ids) {
        return repository.findAllById(ids)
                .map(this::toEntity);
    }
}
