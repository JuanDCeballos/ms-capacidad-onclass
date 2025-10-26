package co.onclass.model.tecnologia.gateways;

import co.onclass.model.paging.SortDirection;
import co.onclass.model.tecnologia.PaginatedCapacidadIdsResponse;
import co.onclass.model.tecnologia.Tecnologia;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TecnologiaGateway {

    Mono<List<Tecnologia>> asignarTecnologias(Long idCapacidad, Set<Long> tecnologias);

    Mono<PaginatedCapacidadIdsResponse> getCapacidadesIdsOrdenadasPorConteo(int page, int size, SortDirection direction);

    Mono<Map<Long, List<Tecnologia>>> getTecnologiasPorCapacidad(List<Long> capacidades);
}
