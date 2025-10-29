package co.onclass.api;

import co.onclass.api.dto.ApiSuccessResponse;
import co.onclass.api.dto.capacidad.CapacidadBootcampRequestDto;
import co.onclass.api.dto.capacidad.CapacidadRequestDto;
import co.onclass.api.dto.capacidad.CapacidadTecnologiaRequestDto;
import co.onclass.api.utils.CapacidadMapper;
import co.onclass.api.validation.ValidationService;
import co.onclass.model.paging.PageableQuery;
import co.onclass.model.paging.SortDirection;
import co.onclass.usecase.capacidad.CapacidadUseCase;
import co.onclass.usecase.capacidadbootcamp.CapacidadBootcampUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static co.onclass.api.constants.ApiConstants.ID_CAPACIDAD_PATH_VARIABLE;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
@RequiredArgsConstructor
public class Handler {

    private final CapacidadUseCase capacidadUseCase;
    private final CapacidadBootcampUseCase capacidadBootcampUseCase;
    private final ValidationService validationService;
    private final CapacidadMapper capacidadMapper;

    public Mono<ServerResponse> listenPOSTGuardarCapacidad(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CapacidadRequestDto.class)
                .flatMap(validationService::validateObject)
                .map(capacidadMapper::toCapacidad)
                .flatMap(capacidadUseCase::guardarCapacidad)
                .map(capacidadMapper::toCapacidadResponseDto)
                .flatMap(capacidadGuardada ->
                        status(201)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(capacidadGuardada))
                );
    }

    public Mono<ServerResponse> listenPOSTGuardarTecnologiasCapacidad(ServerRequest serverRequest) {
        Long idCapacidad = Long.valueOf(serverRequest.pathVariable(ID_CAPACIDAD_PATH_VARIABLE));

        return serverRequest.bodyToMono(CapacidadTecnologiaRequestDto.class)
                .flatMap(validationService::validateObject)
                .flatMap(dto ->
                        capacidadUseCase.guardarTecnologiasCapacidad(idCapacidad, dto.getTecnologias()))
                .map(capacidadMapper::toCapacidadConTecnologias)
                .flatMap(tecnologiasGuardadas ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(tecnologiasGuardadas))
                );
    }

    public Mono<ServerResponse> listenGETCapacidades(ServerRequest serverRequest) {
        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(10);
        String sortBy = serverRequest.queryParam("sortBy").orElse("nombre");
        String order = serverRequest.queryParam("order").orElse("ASC");

        SortDirection direction = "DESC".equalsIgnoreCase(order)
                ? SortDirection.DESC
                : SortDirection.ASC;

        PageableQuery pageableQuery = new PageableQuery(page, size, sortBy, direction);

        return capacidadUseCase.listarCapacidadesPaginadas(pageableQuery)
                .flatMap(paginaDto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(paginaDto)
                );
    }

    public Mono<ServerResponse> listenPOSTAsignarCapacidadesBootcamp(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CapacidadBootcampRequestDto.class)
                .flatMap(dto ->
                        capacidadBootcampUseCase.asignarCapacidadesBootcamp(
                                dto.getIdBootcamp(),
                                dto.getCapacidadesIds()
                        ))
                .flatMap(capacidadesAsignadas ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(capacidadesAsignadas)
                );
    }

    public Mono<ServerResponse> listenGETCapacidadesPorBootcamps(ServerRequest serverRequest) {
        List<Long> bootcampsIds = serverRequest.queryParam("ids")
                .map(idsString -> Arrays.stream(idsString.split(","))
                        .map(Long::parseLong)
                        .toList())
                .orElse(Collections.emptyList());

        return capacidadBootcampUseCase.getCapacidadesTecnologiasPorBootcamps(bootcampsIds)
                .flatMap(res ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(res)
                );
    }

    public Mono<ServerResponse> listenGETBootcampsCantidadCapacidades(ServerRequest serverRequest) {
        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(10);
        String order = serverRequest.queryParam("order").orElse("ASC");

        SortDirection direction = "DESC".equalsIgnoreCase(order)
                ? SortDirection.DESC
                : SortDirection.ASC;

        PageableQuery pageableQuery = new PageableQuery(page, size, "count", direction);

        return capacidadBootcampUseCase.getBootcampPorCantidadCapacidades(pageableQuery)
                .flatMap(res ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(res)
                );
    }
}
