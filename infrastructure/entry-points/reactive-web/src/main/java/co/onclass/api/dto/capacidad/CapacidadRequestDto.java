package co.onclass.api.dto.capacidad;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapacidadRequestDto {

    @NotBlank(message = "El nombre de la capacidad es obligatoria.")
    private String nombre;

    @NotBlank(message = "La descripción de la capacidad es obligatoria.")
    private String descripcion;
}
