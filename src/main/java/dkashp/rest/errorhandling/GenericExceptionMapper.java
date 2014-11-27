package dkashp.rest.errorhandling;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable>{

	public Response toResponse(Throwable ex) {
		ErrorMessage errorMessage = new ErrorMessage();
		setHttpStatus(ex, errorMessage);
		errorMessage.setMessage(ex.getMessage());
		StringWriter errorStackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(errorStackTrace));
		errorMessage.setDeveloperMessage(errorStackTrace.toString());
		return Response.status(errorMessage.getStatusCode())
				.entity(errorMessage)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	private void setHttpStatus(Throwable ex, ErrorMessage errorMessage){
		if(ex instanceof WebApplicationException){
			errorMessage.setStatusCode(((WebApplicationException)ex).getResponse().getStatus());
		} else{
			errorMessage.setStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
		}
	}

}
