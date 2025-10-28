package co.onclass.api.dto.capacidad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapacidadBootcampRequestDto {

    private Long idBootcamp;
    private List<Long> capacidadesIds;
}
