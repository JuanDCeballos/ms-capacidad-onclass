package co.onclass.usecase.capacidadbootcamp;

import co.onclass.exceptions.BusinessException;
import co.onclass.model.capacidad.Capacidad;
import co.onclass.model.capacidad.CapacidadDetallada;
import co.onclass.model.capacidad.gateways.CapacidadRepository;
import co.onclass.model.capacidadbootcamp.CapacidadBootcamp;
import co.onclass.model.capacidadbootcamp.ConteoCapacidadPaginada;
import co.onclass.model.capacidadbootcamp.gateways.CapacidadBootcampRepository;
import co.onclass.model.paging.PageableQuery;
import co.onclass.model.paging.SortDirection;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapacidadBootcampUseCaseTest {

    @InjectMocks
    CapacidadBootcampUseCase capacidadBootcampUseCase;

    @Mock
    CapacidadBootcampRepository capacidadBootcampRepository;

    @Mock
    CapacidadRepository capacidadRepository;

    @Mock
    TecnologiaGateway tecnologiaGateway;

    private Map<Long, List<Tecnologia>> mapaTecnologias;
    private final List<Long> capacidadesIds = List.of(1L, 2L, 3L);
    private List<Long> bootcampsIds = List.of(1L, 2L, 3L);
    private Capacidad capacidad;
    private CapacidadBootcamp capacidadBootcamp;
    private PageableQuery query;
    private Long conteoCapacidades = 3L;
    private Long conteoBootcamps = 2L;
    private final Long idBootcamp = 1L;

    @BeforeEach
    void initMocks() {
        mapaTecnologias = new HashMap<>();
        List<Tecnologia> tecnologias = new ArrayList<>();
        Tecnologia tecnologia = new Tecnologia();
        tecnologia.setId(1L);
        tecnologia.setNombre("Java");
        tecnologias.add(tecnologia);
        mapaTecnologias.put(1L, tecnologias);

        capacidad = new Capacidad();
        capacidad.setId(1L);
        capacidad.setNombre("Desarrollo FrontEnd");
        capacidad.setDescripcion("Capacidad para ser desarrollador frontend");

        capacidadBootcamp = new CapacidadBootcamp();
        capacidadBootcamp.setId(1L);
        capacidadBootcamp.setIdBootcamp(1L);
        capacidadBootcamp.setIdCapacidad(1L);

        query = new PageableQuery();
        query.setPage(0);
        query.setSize(1);
        query.setSortBy("nombre");
        query.setDirection(SortDirection.ASC);
    }

    @Test
    void asignarCapacidadesBootcamp() {
        when(capacidadRepository.contarCapacidadesExistentes(anyList())).thenReturn(Mono.just(conteoCapacidades));
        when(capacidadBootcampRepository.guardarCapacidadesBootcamp(anyList())).thenReturn(Flux.empty());
        when(capacidadRepository.buscarTodasPorId(anyList())).thenReturn(Flux.just(capacidad));
        when(tecnologiaGateway.getTecnologiasPorCapacidad(anyList())).thenReturn(Mono.just(mapaTecnologias));

        Mono<List<CapacidadDetallada>> respuesta =
                capacidadBootcampUseCase.asignarCapacidadesBootcamp(idBootcamp, capacidadesIds);

        StepVerifier.create(respuesta)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertEquals(1L, res.getFirst().getId());
                })
                .verifyComplete();

        verify(capacidadRepository, times(1)).contarCapacidadesExistentes(anyList());
        verify(capacidadBootcampRepository, times(1)).guardarCapacidadesBootcamp(anyList());
        verify(capacidadRepository, times(1)).buscarTodasPorId(anyList());
        verify(tecnologiaGateway, times(1)).getTecnologiasPorCapacidad(anyList());
    }

    @Test
    void asignarCapacidadesBootcampRetornaErrorCapacidadNoExiste() {
        conteoCapacidades = 1L;

        when(capacidadRepository.contarCapacidadesExistentes(anyList())).thenReturn(Mono.just(conteoCapacidades));

        Mono<List<CapacidadDetallada>> respuesta =
                capacidadBootcampUseCase.asignarCapacidadesBootcamp(idBootcamp, capacidadesIds);

        StepVerifier.create(respuesta)
                .expectError(BusinessException.class)
                .verify();

        verify(capacidadRepository, times(1)).contarCapacidadesExistentes(anyList());
        verify(capacidadBootcampRepository, times(0)).guardarCapacidadesBootcamp(anyList());
        verify(capacidadRepository, times(0)).buscarTodasPorId(anyList());
        verify(tecnologiaGateway, times(0)).getTecnologiasPorCapacidad(anyList());
    }

    @Test
    void getCapacidadesTecnologiasPorBootcamps() {
        when(capacidadBootcampRepository.buscarTodasPorIdBootcamp(anyList())).thenReturn(Flux.just(capacidadBootcamp));
        when(capacidadRepository.buscarTodasPorId(anyList())).thenReturn(Flux.just(capacidad));
        when(tecnologiaGateway.getTecnologiasPorCapacidad(anyList())).thenReturn(Mono.just(mapaTecnologias));

        Mono<Map<Long, List<CapacidadDetallada>>> respuesta =
                capacidadBootcampUseCase.getCapacidadesTecnologiasPorBootcamps(bootcampsIds);

        StepVerifier.create(respuesta)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertEquals(1L, res.get(1L).getFirst().getId());
                    assertEquals("Desarrollo FrontEnd", res.get(1L).getFirst().getNombre());
                    assertEquals("Java", res.get(1L).getFirst().getTecnologias().getFirst().getNombre());
                })
                .verifyComplete();

        verify(capacidadBootcampRepository, times(1)).buscarTodasPorIdBootcamp(anyList());
        verify(capacidadRepository, times(1)).buscarTodasPorId(anyList());
        verify(tecnologiaGateway, times(1)).getTecnologiasPorCapacidad(anyList());
    }

    @Test
    void getCapacidadesTecnologiasPorBootcampsRetornaVacio() {
        when(capacidadBootcampRepository.buscarTodasPorIdBootcamp(anyList())).thenReturn(Flux.empty());

        Mono<Map<Long, List<CapacidadDetallada>>> respuesta =
                capacidadBootcampUseCase.getCapacidadesTecnologiasPorBootcamps(bootcampsIds);

        StepVerifier.create(respuesta)
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

        verify(capacidadBootcampRepository, times(1)).buscarTodasPorIdBootcamp(anyList());
        verify(capacidadRepository, times(0)).buscarTodasPorId(anyList());
        verify(tecnologiaGateway, times(0)).getTecnologiasPorCapacidad(anyList());
    }

    @Test
    void getBootcampPorCantidadCapacidades() {
        when(capacidadBootcampRepository.contarBootcampsDistintos()).thenReturn(Mono.just(conteoBootcamps));
        when(capacidadBootcampRepository.
                getBootcampsIdsOrdenadosPorConteoCapacidades(any(PageableQuery.class))).thenReturn(Flux.just(1L));

        Mono<ConteoCapacidadPaginada> respuesta = capacidadBootcampUseCase.getBootcampPorCantidadCapacidades(query);

        StepVerifier.create(respuesta)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertEquals(2L, res.getTotalElementos());
                })
                .verifyComplete();

        verify(capacidadBootcampRepository, times(1)).contarBootcampsDistintos();
        verify(capacidadBootcampRepository, times(1)).
                getBootcampsIdsOrdenadosPorConteoCapacidades(any(PageableQuery.class));
    }
}
