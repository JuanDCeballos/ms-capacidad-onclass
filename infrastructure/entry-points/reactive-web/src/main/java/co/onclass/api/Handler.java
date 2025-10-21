package co.onclass.api;

import co.onclass.api.dto.ApiSuccessResponse;
import co.onclass.api.dto.capacidad.CapacidadRequestDto;
import co.onclass.api.utils.CapacidadMapper;
import co.onclass.api.validation.ValidationService;
import co.onclass.usecase.capacidad.CapacidadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
}
