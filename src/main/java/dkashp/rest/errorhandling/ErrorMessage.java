package dkashp.rest.errorhandling;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.sun.jersey.api.NotFoundException;
import javax.ws.rs.core.Response;

public class ErrorMessage {

	/** contains the same HTTP Status code returned by the server */
	private int statusCode;
	
	/**message describing error*/
	private String message;
	
	/**detailed error message for developers*/
	private String developerMessage;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	
	public ErrorMessage(AppException ex){
		try{
			BeanUtils.copyProperties(this, ex);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ErrorMessage(NotFoundException ex){
		this.statusCode = Response.Status.NOT_FOUND.getStatusCode();
		this.message = ex.getMessage();
	}
	
	public ErrorMessage(){
		
	}
}
