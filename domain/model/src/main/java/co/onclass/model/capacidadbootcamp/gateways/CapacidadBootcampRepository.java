package co.onclass.model.capacidadbootcamp.gateways;

import co.onclass.model.capacidadbootcamp.CapacidadBootcamp;
import co.onclass.model.paging.PageableQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacidadBootcampRepository {

    Flux<Void> guardarCapacidadesBootcamp(List<CapacidadBootcamp> relaciones);

    Flux<CapacidadBootcamp> buscarTodasPorIdBootcamp(List<Long> bootcampsIds);

    Mono<Long> contarBootcampsDistintos();

    Flux<Long> getBootcampsIdsOrdenadosPorConteoCapacidades(PageableQuery query);
}
