package dkashp.rest.response;

import java.util.List;

public class RestResponseForSquare<T> {
	private String status;
	private String message;
	private List<T> squares;
	
	public RestResponseForSquare(){
		
	}
	public RestResponseForSquare(String status, String message, List<T> squares) {
		this.status = status;
		this.message = message;
		this.squares = squares;
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
	public List<T> getSquares() {
		return squares;
	}
	public void setSquares(List<T> squares) {
		this.squares = squares;
	}
}
