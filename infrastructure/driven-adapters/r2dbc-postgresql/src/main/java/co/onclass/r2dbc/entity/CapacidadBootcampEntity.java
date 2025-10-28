package co.onclass.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("capacidad_bootcamps")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CapacidadBootcampEntity {

    @Id
    private Long id;

    @Column("id_capacidad")
    private Long idCapacidad;

    @Column("id_bootcamp")
    private Long idBootcamp;
}
