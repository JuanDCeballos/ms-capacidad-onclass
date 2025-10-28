package co.onclass.consumer;

import co.onclass.consumer.dto.ExternalApiErrorResponse;
import co.onclass.consumer.dto.tecnologia.CapacidadTecnologiaRequestDto;
import co.onclass.enums.ExceptionMessages;
import co.onclass.exceptions.BusinessException;
import co.onclass.exceptions.TechnicalException;
import co.onclass.model.paging.SortDirection;
import co.onclass.model.tecnologia.PaginatedCapacidadIdsResponse;
import co.onclass.model.tecnologia.Tecnologia;
import co.onclass.model.tecnologia.gateways.TecnologiaGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TecnologiaRestConsumer implements TecnologiaGateway {

    private final WebClient client;

    private static final String URI_ASIGNAR_TECNOLOGIAS = "/api/v1/tecnologia/asignar-tecnologia";
    private static final String URI_CAPACIDADES_ORDENADAS = "/api/v1/tecnologia/capacidades-ordenadas";
    private static final String URI_TECNOLOGIAS_POR_CAPACIDADES = "/api/v1/tecnologia/por-tecnologia";

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
                                    return Mono.error(new BusinessException(ExceptionMessages.ERROR_DE_NEGOCIO_EN_MICROSERVICIO_EXTERNO, errorMessage));
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

    @Override
    public Mono<PaginatedCapacidadIdsResponse> getCapacidadesIdsOrdenadasPorConteo(int page, int size, SortDirection direction) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CAPACIDADES_ORDENADAS)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("order", direction.name())
                        .build()
                ).retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, res ->
                        res.bodyToMono(ExternalApiErrorResponse.class)
                                .flatMap(errorBody -> {
                                    String errorMessage = String.join(", ", errorBody.getDetails());
                                    return Mono.error(new BusinessException(ExceptionMessages.ERROR_DE_NEGOCIO_EN_MICROSERVICIO_EXTERNO, errorMessage));
                                })
                )
                .onStatus(HttpStatusCode::is5xxServerError, res ->
                        res.bodyToMono(ExternalApiErrorResponse.class)
                                .flatMap(errorBody ->
                                        Mono.error(new TechnicalException(ExceptionMessages.ERROR_TECNICO_EN_MICROSERVICIO_EXTERNO))
                                )
                )
                .bodyToMono(PaginatedCapacidadIdsResponse.class);
    }

    @Override
    public Mono<Map<Long, List<Tecnologia>>> getTecnologiasPorCapacidad(List<Long> capacidades) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_TECNOLOGIAS_POR_CAPACIDADES)
                        .queryParam("ids", capacidades.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(",")))
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, res ->
                        res.bodyToMono(ExternalApiErrorResponse.class)
                                .flatMap(errorBody -> {
                                    String errorMessage = String.join(", ", errorBody.getDetails());
                                    return Mono.error(new BusinessException(ExceptionMessages.ERROR_DE_NEGOCIO_EN_MICROSERVICIO_EXTERNO, errorMessage));
                                })
                )
                .onStatus(HttpStatusCode::is5xxServerError, res ->
                        res.bodyToMono(ExternalApiErrorResponse.class)
                                .flatMap(errorBody ->
                                        Mono.error(new TechnicalException(ExceptionMessages.ERROR_TECNICO_EN_MICROSERVICIO_EXTERNO))
                                )
                )
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }
}
