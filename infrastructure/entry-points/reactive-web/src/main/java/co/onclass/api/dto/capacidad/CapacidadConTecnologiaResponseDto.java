package co.onclass.api.dto.capacidad;

import co.onclass.api.dto.tecnologia.TecnologiaResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapacidadConTecnologiaResponseDto {
    
    private Long id;
    private String nombre;
    private List<TecnologiaResponseDto> tecnologias;
}
