package co.onclass.api.dto.tecnologia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TecnologiaResponseDto {
    private Long id;
    private String nombre;
}
