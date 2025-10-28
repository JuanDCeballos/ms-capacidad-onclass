package co.onclass.model.capacidadbootcamp;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapacidadBootcamp {

    private Long id;
    private Long idCapacidad;
    private Long idBootcamp;
}
