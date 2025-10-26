package co.onclass.model.paging;

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
public class PageableQuery {

    private int page;
    private int size;
    private String sortBy;
    private SortDirection direction;
}
