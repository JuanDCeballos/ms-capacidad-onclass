package co.onclass.usecase.capacidad;

import co.onclass.enums.ExceptionMessages;
import co.onclass.exceptions.BusinessException;
import co.onclass.model.capacidad.Capacidad;
import co.onclass.model.capacidad.CapacidadDetallada;
import co.onclass.model.capacidad.gateways.CapacidadRepository;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class CapacidadUseCase {

    private final CapacidadRepository capacidadRepository;
    private final TecnologiaGateway tecnologiaGateway;

    public Mono<Capacidad> guardarCapacidad(Capacidad capacidad) {
        return capacidadRepository.guardarCapacidad(capacidad);
    }

    public Mono<CapacidadDetallada> guardarTecnologiasCapacidad(Long idCapacidad, List<Long> tecnologias) {
        Mono<Set<Long>> tecnologiasValidas = validarListaTecnologias(tecnologias);

        return tecnologiasValidas.flatMap(tecnologiasSet -> {
            Mono<Capacidad> capacidadMono = capacidadRepository.buscarPorId(idCapacidad)
                    .switchIfEmpty(Mono.error(new BusinessException(ExceptionMessages.CAPACIDAD_NO_ENCONTRADA)));

            Mono<List<Tecnologia>> tecnologiasMono = tecnologiaGateway.asignarTecnologias(idCapacidad, tecnologiasSet);

            return Mono.zip(capacidadMono, tecnologiasMono)
                    .map(tupla -> {
                        Capacidad capacidad = tupla.getT1();
                        List<Tecnologia> tecnologiasAsignadas = tupla.getT2();

                        return new CapacidadDetallada(
                                capacidad.getId(),
                                capacidad.getNombre(),
                                tecnologiasAsignadas
                        );
                    });
        });
    }

    private Mono<Set<Long>> validarListaTecnologias(List<Long> tecnologias) {
        Set<Long> idTecnologias = new HashSet<>(tecnologias);

        if (idTecnologias.size() != tecnologias.size()) {
            return Mono.error(new BusinessException(ExceptionMessages.CAPACIDAD_CON_TECNOLOGIAS_DUPLICADAS));
        }

        if (idTecnologias.size() < 3 || idTecnologias.size() > 20) {
            return Mono.error(new BusinessException(ExceptionMessages.MONTO_TECNOLOGIAS_CAPACIDAD));
        }

        return Mono.just(idTecnologias);
    }
}
