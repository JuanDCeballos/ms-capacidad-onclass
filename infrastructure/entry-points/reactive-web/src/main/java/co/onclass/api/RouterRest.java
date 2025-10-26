package co.onclass.api;

import co.onclass.api.dto.ApiErrorResponse;
import co.onclass.api.dto.ApiSuccessResponse;
import co.onclass.api.dto.capacidad.CapacidadRequestDto;
import co.onclass.api.dto.capacidad.CapacidadTecnologiaRequestDto;
import co.onclass.model.paging.PaginaDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static co.onclass.api.constants.ApiConstants.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = GUARDAR_CAPACIDAD,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenPOSTGuardarCapacidad",
                    operation = @Operation(
                            operationId = "guardarCapacidad",
                            summary = "Guardar una nueva capacidad",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos de la nueva capacidad a crear",
                                    content = @Content(schema = @Schema(implementation = CapacidadRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Capacidad guardada exitosamente",
                                            content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Datos invalidos",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = AGREGAR_TECNOLOGIAS,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenPOSTGuardarTecnologiasCapacidad",
                    operation = @Operation(
                            operationId = "guardarTecnologiasCapacidad",
                            summary = "Guarda las tecnologías que le pertenecen a una capacidad",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH,
                                            name = ID_CAPACIDAD_PATH_VARIABLE,
                                            description = "Id de la capacidad",
                                            required = true
                                    )
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Lista de las tecnologías a relacionar",
                                    content = @Content(schema = @Schema(implementation = CapacidadTecnologiaRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Tecnologías relacionadas a la capacidad exitosamente",
                                            content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Datos invalidos",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = OBTENER_CAPACIDADES,
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGETCapacidades",
                    operation = @Operation(
                            operationId = "listarCapacidadesPaginadas",
                            summary = "lista las capacidades paginadas y filtradas",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "page",
                                            description = "Página de la petición"
                                    ),
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "size",
                                            description = "Tamaño de la petición por página"
                                    ),
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "sortBy",
                                            description = "Campo por el cuál se ordenará la petición"
                                    ),
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "order",
                                            description = "Orden de los elementos de la petición"
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Tecnologías relacionadas a la capacidad exitosamente",
                                            content = @Content(schema = @Schema(implementation = PaginaDto.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Datos invalidos",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(GUARDAR_CAPACIDAD), handler::listenPOSTGuardarCapacidad)
                .andRoute(POST(AGREGAR_TECNOLOGIAS), handler::listenPOSTGuardarTecnologiasCapacidad)
                .andRoute(GET(OBTENER_CAPACIDADES), handler::listenGETCapacidades);
    }
}
