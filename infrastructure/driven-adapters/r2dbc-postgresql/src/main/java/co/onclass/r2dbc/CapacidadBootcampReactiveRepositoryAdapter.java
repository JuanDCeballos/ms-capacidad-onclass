package co.onclass.r2dbc;

import co.onclass.model.capacidadbootcamp.CapacidadBootcamp;
import co.onclass.model.capacidadbootcamp.gateways.CapacidadBootcampRepository;
import co.onclass.model.paging.PageableQuery;
import co.onclass.model.paging.SortDirection;
import co.onclass.r2dbc.entity.CapacidadBootcampEntity;
import co.onclass.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class CapacidadBootcampReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        CapacidadBootcamp,
        CapacidadBootcampEntity,
        Long,
        CapacidadBootcampReactiveRepository
        > implements CapacidadBootcampRepository {

    private final DatabaseClient databaseClient;

    public CapacidadBootcampReactiveRepositoryAdapter(CapacidadBootcampReactiveRepository repository,
                                                      ObjectMapper mapper, DatabaseClient databaseClient) {
        super(repository, mapper, d -> mapper.map(d, CapacidadBootcamp.class));
        this.databaseClient = databaseClient;
    }

    @Override
    public Flux<Void> guardarCapacidadesBootcamp(List<CapacidadBootcamp> relaciones) {
        List<CapacidadBootcampEntity> entities = relaciones.stream()
                .map(rel -> mapper.map(rel, CapacidadBootcampEntity.class))
                .toList();

        return repository.saveAll(entities).then().flux();
    }

    @Override
    public Flux<CapacidadBootcamp> buscarTodasPorIdBootcamp(List<Long> bootcampsIds) {
        return repository.findAllByIdBootcampIn(bootcampsIds)
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> contarBootcampsDistintos() {
        String sql = "SELECT COUNT(DISTINCT id_bootcamp) FROM capacidad_bootcamps";

        return databaseClient.sql(sql)
                .map(row -> row.get(0, Long.class))
                .one();
    }

    @Override
    public Flux<Long> getBootcampsIdsOrdenadosPorConteoCapacidades(PageableQuery query) {
        String orderSql = query.getDirection() == SortDirection.DESC ? "DESC" : "ASC";
        long offset = (long) query.getPage() * query.getSize();
        int limit = query.getSize();

        String sql = String.format(
                "SELECT id_bootcamp FROM capacidad_bootcamps " +
                        "GROUP BY id_bootcamp " +
                        "ORDER BY COUNT(id_capacidad) %s " +
                        "OFFSET %d LIMIT %d",
                orderSql, offset, limit
        );

        return databaseClient.sql(sql)
                .map(row -> row.get("id_bootcamp", Long.class))
                .all();
    }
}
