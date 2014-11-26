package dkashp.rest.errorhandling;

public class AppException extends Exception{

	/**
	 * Class to map application related exceptions
	 */
	private static final long serialVersionUID = -2128004960656888953L;

	/**HTTP status of the response*/
	private int statusCode;
	
	/**detailed error message for developers*/
	private String developerMessage;

	public AppException(int statusCode, String message, String developerMessage) {
		super(message);
		this.statusCode = statusCode;
		this.developerMessage = developerMessage;
	}
	
	public AppException(){
		
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	
}
