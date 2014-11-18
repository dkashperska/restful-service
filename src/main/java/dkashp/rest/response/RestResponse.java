package dkashp.rest.response;

public class RestResponse<T> {

	private String status;
	private String message;
	private T object;
	
	public RestResponse(){
		
	}
	public RestResponse(String status, String message, T object) {
		this.status = status;
		this.message = message;
		this.object = object;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getObject() {
		return object;
	}
	public void setObject(T object) {
		this.object = object;
	}
}
