package co.onclass.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalApiErrorResponse {

    @Builder.Default
    private boolean success = false;
    private String errorMessage;
    private List<String> details;
    private String timestamp;
    private String path;
}
