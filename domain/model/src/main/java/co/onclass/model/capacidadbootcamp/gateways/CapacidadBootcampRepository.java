package co.onclass.model.capacidadbootcamp.gateways;

import co.onclass.model.capacidadbootcamp.CapacidadBootcamp;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CapacidadBootcampRepository {

    Flux<Void> guardarCapacidadesBootcamp(List<CapacidadBootcamp> relaciones);
}
