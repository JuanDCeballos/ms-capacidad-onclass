package co.onclass.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorResponse {

    @Builder.Default
    private boolean success = false;
    private String errorMessage;
    private Object details;
    private String timestamp;
    private String path;
}
