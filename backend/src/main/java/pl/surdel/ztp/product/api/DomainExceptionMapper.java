package pl.surdel.ztp.product.api;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.surdel.ztp.product.api.dto.ErrorResponse;
import pl.surdel.ztp.product.domain.exception.DomainValidationException;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainValidationException> {
    @Override
    public Response toResponse(DomainValidationException exception) {
        return Response
                .status(422)
                .entity(new ErrorResponse(exception.getErrors()))
                .build();
    }
}
