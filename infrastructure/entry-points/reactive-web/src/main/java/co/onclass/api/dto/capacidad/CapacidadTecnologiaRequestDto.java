package co.onclass.api.dto.capacidad;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapacidadTecnologiaRequestDto {

    @NotEmpty(message = "La lista de tecnologías es obligatoria.")
    private List<Long> tecnologias;
}
