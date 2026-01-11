package pl.surdel.ztp.product.api.dto;

import java.util.List;

public class ErrorResponse {
    public List<String> errors;

    public ErrorResponse(List<String> errors) {
        this.errors = errors;
    }
}
