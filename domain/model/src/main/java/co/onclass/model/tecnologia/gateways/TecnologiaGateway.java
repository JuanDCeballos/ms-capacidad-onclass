package co.onclass.model.tecnologia.gateways;

import co.onclass.model.tecnologia.Tecnologia;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface TecnologiaGateway {

    Mono<List<Tecnologia>> asignarTecnologias(Long idCapacidad, Set<Long> tecnologias);
}
