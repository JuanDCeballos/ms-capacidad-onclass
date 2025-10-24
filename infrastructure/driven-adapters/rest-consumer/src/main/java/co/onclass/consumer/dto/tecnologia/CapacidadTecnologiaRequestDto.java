package co.onclass.consumer.dto.tecnologia;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class CapacidadTecnologiaRequestDto {
    private Long idCapacidad;
    private Collection<Long> tecnologias;
}
