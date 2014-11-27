package dkashp.rest.errorhandling;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.api.NotFoundException;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException>{

	public Response toResponse(NotFoundException ex) {
		return Response.status(ex.getResponse().getStatus())
				.entity(new ErrorMessage(ex))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

}
