package co.onclass.model.tecnologia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedCapacidadIdsResponse {

    private long totalElementos;
    private List<Long> ids;
}
