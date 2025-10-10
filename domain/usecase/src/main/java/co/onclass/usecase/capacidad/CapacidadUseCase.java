package co.onclass.usecase.capacidad;

import co.onclass.model.capacidad.Capacidad;
import co.onclass.model.capacidad.gateways.CapacidadRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacidadUseCase {

    private final CapacidadRepository capacidadRepository;

    public Mono<Capacidad> guardarCapacidad(Capacidad capacidad) {
        return capacidadRepository.guardarCapacidad(capacidad);
    }

}
