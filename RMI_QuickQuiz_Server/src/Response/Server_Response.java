package Response;

import java.io.Serializable;

public class Server_Response<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String action;
	private String status;
	private String message;
	private T result;
	
	public Server_Response() {
	}
	

	public Server_Response(String action, String status, String message, T result) {
		this.action = action;
		this.status = status;
		this.message = message;
		this.result = result;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
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

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	
	
	@Override
	public String toString() {
		return "[status=" + status + ", message=" + message + ", result=" + result + "]";
	}

}

