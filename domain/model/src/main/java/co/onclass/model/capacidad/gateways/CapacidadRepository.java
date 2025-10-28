package co.onclass.model.capacidad.gateways;

import co.onclass.model.capacidad.Capacidad;
import co.onclass.model.paging.PageableQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacidadRepository {

    Mono<Capacidad> guardarCapacidad(Capacidad capacidad);

    Mono<Capacidad> buscarPorId(Long idCapacidad);

    Mono<Long> contarTodos();

    Flux<Capacidad> buscarTodasPaginadas(PageableQuery query);

    Flux<Capacidad> buscarTodasPorIdEnOrden(List<Long> ids);

    Mono<Long> contarCapacidadesExistentes(List<Long> ids);

    Flux<Capacidad> buscarTodasPorId(List<Long> ids);
}
