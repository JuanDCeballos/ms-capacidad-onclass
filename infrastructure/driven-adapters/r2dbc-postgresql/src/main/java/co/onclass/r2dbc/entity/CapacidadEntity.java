package co.onclass.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("capacidades")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CapacidadEntity {

    @Id
    private Long id;

    private String nombre;
    private String descripcion;
}
