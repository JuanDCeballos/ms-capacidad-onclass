package co.onclass.usecase.capacidadbootcamp;

import co.onclass.enums.ExceptionMessages;
import co.onclass.exceptions.BusinessException;
import co.onclass.model.capacidad.Capacidad;
import co.onclass.model.capacidad.CapacidadDetallada;
import co.onclass.model.capacidad.gateways.CapacidadRepository;
import co.onclass.model.capacidadbootcamp.CapacidadBootcamp;
import co.onclass.model.capacidadbootcamp.gateways.CapacidadBootcampRepository;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CapacidadBootcampUseCase {

    private final CapacidadBootcampRepository capacidadBootcampRepository;
    private final CapacidadRepository capacidadRepository;
    private final TecnologiaGateway tecnologiaGateway;

    public Mono<List<CapacidadDetallada>> asignarCapacidadesBootcamp(Long idBootcamp, List<Long> capacidadesIds) {
        Mono<Void> validacion = capacidadRepository.contarCapacidadesExistentes(capacidadesIds)
                .flatMap(count -> {
                    if (count != capacidadesIds.size()) {
                        return Mono.error(new BusinessException(ExceptionMessages.CAPACIDAD_NO_ENCONTRADA));
                    }
                    return Mono.empty();
                });

        Mono<Void> guardar = Mono.defer(() -> {
            List<CapacidadBootcamp> relaciones = capacidadesIds.stream()
                    .map(idCap -> new CapacidadBootcamp(null, idCap, idBootcamp))
                    .toList();
            return capacidadBootcampRepository.guardarCapacidadesBootcamp(relaciones).then();
        });

        Mono<List<CapacidadDetallada>> datosFinal = Mono.defer(() -> {
            Mono<List<Capacidad>> capacidadesMono = capacidadRepository.buscarTodasPorId(capacidadesIds).collectList();

            Mono<Map<Long, List<Tecnologia>>> tecnologiasMapMono =
                    tecnologiaGateway.getTecnologiasPorCapacidad(capacidadesIds);

            return Mono.zip(capacidadesMono, tecnologiasMapMono)
                    .map(tuple -> {
                        List<Capacidad> capacidades = tuple.getT1();
                        Map<Long, List<Tecnologia>> tecnoMap = tuple.getT2();

                        return capacidades.stream()
                                .map(cap -> ensamblarDto(
                                        cap,
                                        tecnoMap.getOrDefault(cap.getId(), Collections.emptyList())
                                ))
                                .toList();
                    });
        });

        return validacion
                .then(guardar)
                .then(datosFinal);
    }

    private CapacidadDetallada ensamblarDto(Capacidad capacidad, List<Tecnologia> tecnologias) {
        if (capacidad == null) return null;
        return CapacidadDetallada.builder()
                .id(capacidad.getId())
                .nombre(capacidad.getNombre())
                .tecnologias(tecnologias)
                .build();
    }
}
