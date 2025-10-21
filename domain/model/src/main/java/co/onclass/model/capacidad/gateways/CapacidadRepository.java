package co.onclass.model.capacidad.gateways;

import co.onclass.model.capacidad.Capacidad;
import reactor.core.publisher.Mono;

public interface CapacidadRepository {

    Mono<Capacidad> guardarCapacidad(Capacidad capacidad);
}
