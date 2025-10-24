package co.onclass.consumer;

import co.onclass.consumer.dto.ExternalApiErrorResponse;
import co.onclass.consumer.dto.tecnologia.CapacidadTecnologiaRequestDto;
import co.onclass.enums.ExceptionMessages;
import co.onclass.exceptions.BusinessException;
import co.onclass.exceptions.TechnicalException;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TecnologiaRestConsumer implements TecnologiaGateway {

    private final WebClient client;

    private static final String URI_ASIGNAR_TECNOLOGIAS = "/api/v1/tecnologia/asignar-tecnologia";

    @Override
    public Mono<List<Tecnologia>> asignarTecnologias(Long idCapacidad, Set<Long> tecnologias) {
        CapacidadTecnologiaRequestDto req = CapacidadTecnologiaRequestDto.builder()
                .idCapacidad(idCapacidad)
                .tecnologias(tecnologias)
                .build();

        return client
                .post()
                .uri(URI_ASIGNAR_TECNOLOGIAS)
                .bodyValue(req)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, res ->
                        res.bodyToMono(ExternalApiErrorResponse.class)
                                .flatMap(errorBody -> {
                                    String errorMessage = String.join(", ", errorBody.getDetails());
                                    return Mono.error(new BusinessException(ExceptionMessages.ERROR_EN_MICROSERVICIO_EXTERNO, errorMessage));
                                })
                )
                .onStatus(HttpStatusCode::is5xxServerError, res ->
                        res.bodyToMono(ExternalApiErrorResponse.class)
                                .flatMap(errorBody ->
                                        Mono.error(new TechnicalException(ExceptionMessages.ERROR_TECNICO_EN_MICROSERVICIO_EXTERNO))
                                )
                )
                .bodyToFlux(Tecnologia.class)
                .collectList();
    }
}
