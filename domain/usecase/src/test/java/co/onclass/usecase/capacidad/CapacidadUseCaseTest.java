package co.onclass.usecase.capacidad;

import co.onclass.model.capacidad.Capacidad;
import co.onclass.model.capacidad.gateways.CapacidadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapacidadUseCaseTest {

    @InjectMocks
    CapacidadUseCase capacidadUseCase;

    @Mock
    CapacidadRepository capacidadRepository;

    private Capacidad capacidad;

    @BeforeEach
    void initMocks() {
        capacidad = new Capacidad();
        capacidad.setId(1L);
        capacidad.setNombre("Frontend");
        capacidad.setDescripcion("Capacidad para ser desarrollador Frontend");
    }

    @Test
    void guardarCapacidad() {
        when(capacidadRepository.guardarCapacidad(any(Capacidad.class))).thenReturn(Mono.just(capacidad));

        Mono<Capacidad> respuesta = capacidadUseCase.guardarCapacidad(capacidad);

        StepVerifier.create(respuesta)
                .expectNextMatches(val -> val.equals(capacidad))
                .verifyComplete();

        verify(capacidadRepository, times(1)).guardarCapacidad(any(Capacidad.class));
    }
}
