package co.onclass.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessages {

    CAPACIDAD_NO_ENCONTRADA(404, "La capacidad no fue encontrada."),
    CAPACIDAD_CON_TECNOLOGIAS_DUPLICADAS(400, "La capacidad no permite tecnologías duplicadas."),
    MONTO_TECNOLOGIAS_CAPACIDAD(400, "La capacidad permite entre 3 y 20 tecnologías."),
    ERROR_EN_MICROSERVICIO_EXTERNO(400, "Error en microservicio externo."),
    ERROR_TECNICO_EN_MICROSERVICIO_EXTERNO(500, "Error técnico en microservicio externo."),
    ;

    private final int code;
    private final String message;
}
