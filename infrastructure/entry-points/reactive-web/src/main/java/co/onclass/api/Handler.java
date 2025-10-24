package co.onclass.api;

import co.onclass.api.dto.ApiSuccessResponse;
import co.onclass.api.dto.capacidad.CapacidadConTecnologiaResponseDto;
import co.onclass.api.dto.capacidad.CapacidadRequestDto;
import co.onclass.api.dto.capacidad.CapacidadTecnologiaRequestDto;
import co.onclass.api.utils.CapacidadMapper;
import co.onclass.api.validation.ValidationService;
import co.onclass.usecase.capacidad.CapacidadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.onclass.api.constants.ApiConstants.ID_CAPACIDAD_PATH_VARIABLE;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
@RequiredArgsConstructor
public class Handler {

    private final CapacidadUseCase capacidadUseCase;
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
                .flatMap(tecnologiasGuardadas -> {
                    CapacidadConTecnologiaResponseDto responseDto =
                            capacidadMapper.toCapacidadConTecnologias(tecnologiasGuardadas);

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ApiSuccessResponse<>(responseDto));
                });
    }
}
