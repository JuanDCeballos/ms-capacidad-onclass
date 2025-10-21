package co.onclass.api.dto.capacidad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapacidadResponseDto {

    private Long id;
    private String nombre;
    private String descripcion;
}
