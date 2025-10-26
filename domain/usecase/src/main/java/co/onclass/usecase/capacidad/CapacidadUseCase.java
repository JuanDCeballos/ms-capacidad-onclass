package co.onclass.usecase.capacidad;

import co.onclass.enums.ExceptionMessages;
import co.onclass.exceptions.BusinessException;
import co.onclass.model.capacidad.Capacidad;
import co.onclass.model.capacidad.CapacidadDetallada;
import co.onclass.model.capacidad.gateways.CapacidadRepository;
import co.onclass.model.paging.PageableQuery;
import co.onclass.model.paging.PaginaDto;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public Mono<PaginaDto<CapacidadDetallada>> listarCapacidadesPaginadas(PageableQuery query) {
        if ("technologyCount".equalsIgnoreCase(query.getSortBy())) {
            return listarOrdenadoPorTecnologia(query);
        } else {
            return listarOrdenadoPorCapacidad(query);
        }
    }

    private Mono<PaginaDto<CapacidadDetallada>> listarOrdenadoPorCapacidad(PageableQuery query) {
        Mono<Long> totalElementosMono = capacidadRepository.contarTodos();
        Flux<Capacidad> capacidadFlux = capacidadRepository.buscarTodasPaginadas(query);

        return Mono.zip(totalElementosMono, capacidadFlux.collectList())
                .flatMap(tuple -> {
                    long totalElementos = tuple.getT1();
                    List<Capacidad> capacidades = tuple.getT2();

                    if (capacidades.isEmpty()) {
                        return Mono.just(crearPaginaVacia(query.getPage(), query.getSize(), totalElementos));
                    }

                    List<Long> capacidadesIds = capacidades.stream().map(Capacidad::getId).toList();

                    return tecnologiaGateway.getTecnologiasPorCapacidad(capacidadesIds)
                            .defaultIfEmpty(Collections.emptyMap())
                            .map(mapTecnologias -> {
                                List<CapacidadDetallada> contenido = capacidades.stream()
                                        .map(capacidad -> ensamblarDto(
                                                capacidad,
                                                mapTecnologias.getOrDefault(capacidad.getId(), Collections.emptyList())
                                        )).toList();

                                return crearPaginaDto(query.getPage(), query.getSize(), totalElementos, contenido);
                            });
                });

    }

    private Mono<PaginaDto<CapacidadDetallada>> listarOrdenadoPorTecnologia(PageableQuery query) {
        return tecnologiaGateway.getCapacidadesIdsOrdenadasPorConteo(
                        query.getPage(), query.getSize(), query.getDirection()
                )
                .flatMap(res -> {
                    long totalElementos = res.getTotalElementos();
                    List<Long> capacidadIds = res.getIds();

                    if (capacidadIds.isEmpty()) {
                        return Mono.just(crearPaginaVacia(query.getPage(), query.getSize(), totalElementos));
                    }

                    Mono<List<Capacidad>> capacidadesMono = capacidadRepository.buscarTodasPorIdEnOrden(capacidadIds)
                            .collectList();

                    Mono<Map<Long, List<Tecnologia>>> tecnologiasMono =
                            tecnologiaGateway.getTecnologiasPorCapacidad(capacidadIds)
                                    .defaultIfEmpty(Collections.emptyMap());

                    return Mono.zip(capacidadesMono, tecnologiasMono)
                            .map(tuple -> {
                                List<Capacidad> capacidades = tuple.getT1();
                                Map<Long, List<Tecnologia>> mapTecnologias = tuple.getT2();

                                Map<Long, Capacidad> mapaCapacidades = capacidades.stream()
                                        .collect(Collectors.toMap(Capacidad::getId, Function.identity()));

                                List<CapacidadDetallada> contenido = capacidadIds.stream()
                                        .map(id -> {
                                            Capacidad cap = mapaCapacidades.get(id);
                                            List<Tecnologia> tecnologiaList =
                                                    mapTecnologias.getOrDefault(id, Collections.emptyList());

                                            return ensamblarDto(cap, tecnologiaList);
                                        })
                                        .filter(Objects::nonNull)
                                        .toList();

                                return crearPaginaDto(query.getPage(), query.getSize(), totalElementos, contenido);
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

    private CapacidadDetallada ensamblarDto(Capacidad capacidad, List<Tecnologia> tecnologias) {
        if (capacidad == null) return null;
        return CapacidadDetallada.builder()
                .id(capacidad.getId())
                .nombre(capacidad.getNombre())
                .tecnologias(tecnologias)
                .build();
    }

    private PaginaDto<CapacidadDetallada> crearPaginaDto(
            int page, int size, long totalElementos, List<CapacidadDetallada> contenido) {
        long totalPaginas = (size <= 0) ? 1 : (long) Math.ceil((double) totalElementos / (double) size);
        return PaginaDto.<CapacidadDetallada>builder()
                .paginaActual(page)
                .tamanoPagina(size)
                .totalElementos(totalElementos)
                .totalPaginas((int) totalPaginas)
                .contenido(contenido)
                .build();
    }

    private PaginaDto<CapacidadDetallada> crearPaginaVacia(int page, int size, long totalElementos) {
        return crearPaginaDto(page, size, totalElementos, Collections.emptyList());
    }
}
