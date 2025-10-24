package co.onclass.model.capacidad;

import co.onclass.model.tecnologia.Tecnologia;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapacidadDetallada {

    private Long id;
    private String nombre;
    private List<Tecnologia> tecnologias;
}
