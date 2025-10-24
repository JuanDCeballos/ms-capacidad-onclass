package co.onclass.usecase.capacidad;

import co.onclass.exceptions.BusinessException;
import co.onclass.model.capacidad.Capacidad;
import co.onclass.model.capacidad.CapacidadDetallada;
import co.onclass.model.capacidad.gateways.CapacidadRepository;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapacidadUseCaseTest {

    @InjectMocks
    CapacidadUseCase capacidadUseCase;

    @Mock
    CapacidadRepository capacidadRepository;

    @Mock
    TecnologiaGateway tecnologiaGateway;

    private Capacidad capacidad;
    private CapacidadDetallada capacidadDetallada;
    private Tecnologia tecnologia;

    private List<Tecnologia> tecnologias;
    private List<Long> tecnologiasIds = List.of(1L, 2L, 3L);

    private Long idCapacidad = 1L;

    @BeforeEach
    void initMocks() {
        capacidad = new Capacidad();
        capacidad.setId(1L);
        capacidad.setNombre("Frontend");
        capacidad.setDescripcion("Capacidad para ser desarrollador Frontend");

        tecnologias = new ArrayList<>();
        tecnologia = new Tecnologia();
        tecnologia.setId(1L);
        tecnologia.setNombre("JavaScript");
        tecnologias.add(tecnologia);

        capacidadDetallada = new CapacidadDetallada();
        capacidadDetallada.setId(1L);
        capacidadDetallada.setNombre("Frontend");
        capacidadDetallada.setTecnologias(tecnologias);

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

    @Test
    void guardarTecnologiasCapacidad() {
        when(capacidadRepository.buscarPorId(anyLong())).thenReturn(Mono.just(capacidad));
        when(tecnologiaGateway.asignarTecnologias(anyLong(), anySet())).thenReturn(Mono.just(tecnologias));

        Mono<CapacidadDetallada> respuesta = capacidadUseCase.guardarTecnologiasCapacidad(idCapacidad, tecnologiasIds);

        StepVerifier.create(respuesta)
                .assertNext(dto -> {
                    assertNotNull(dto);
                    assertEquals(1L, dto.getId());
                    assertEquals("Frontend", dto.getNombre());
                })
                .verifyComplete();

        verify(capacidadRepository, times(1)).buscarPorId(anyLong());
        verify(tecnologiaGateway, times(1)).asignarTecnologias(anyLong(), anySet());
    }

    @Test
    void guardarTecnologiasCapacidadRetornaExceptionCuandoListaTieneRepetidos() {
        tecnologiasIds = List.of(1L, 1L, 2L, 2L, 3L, 3L);

        Mono<CapacidadDetallada> respuesta = capacidadUseCase.guardarTecnologiasCapacidad(idCapacidad, tecnologiasIds);

        StepVerifier.create(respuesta)
                .expectError(BusinessException.class)
                .verify();

        verify(capacidadRepository, times(0)).buscarPorId(anyLong());
        verify(tecnologiaGateway, times(0)).asignarTecnologias(anyLong(), anySet());
    }

    @Test
    void guardarTecnologiasCapacidadRetornaExceptionCuandoListaNoCumpleSizeMinimoTres() {
        tecnologiasIds = List.of(1L);

        Mono<CapacidadDetallada> respuesta = capacidadUseCase.guardarTecnologiasCapacidad(idCapacidad, tecnologiasIds);

        StepVerifier.create(respuesta)
                .expectError(BusinessException.class)
                .verify();

        verify(capacidadRepository, times(0)).buscarPorId(anyLong());
        verify(tecnologiaGateway, times(0)).asignarTecnologias(anyLong(), anySet());
    }

    @Test
    void guardarTecnologiasCapacidadRetornaExceptionCuandoListaNoCumpleSizeMaximoVeinte() {
        tecnologiasIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L,
                13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L);

        Mono<CapacidadDetallada> respuesta = capacidadUseCase.guardarTecnologiasCapacidad(idCapacidad, tecnologiasIds);

        StepVerifier.create(respuesta)
                .expectError(BusinessException.class)
                .verify();

        verify(capacidadRepository, times(0)).buscarPorId(anyLong());
        verify(tecnologiaGateway, times(0)).asignarTecnologias(anyLong(), anySet());
    }
}
