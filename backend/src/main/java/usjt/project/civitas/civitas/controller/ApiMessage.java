package usjt.project.civitas.civitas.controller;

import javax.servlet.http.HttpServletRequest;

public class ApiMessage {
	
	private String message;
	private String host;
	private Long timestamp;

	public static ApiMessage buildMessage(String message, HttpServletRequest request) {
		return new ApiMessage(message, request.getRequestURL().toString(), System.currentTimeMillis());
	}
	
	public ApiMessage() {
		super();
	}

	public ApiMessage(String message, String host, Long timestamp) {
		super();
		this.message = message;
		this.host = host;
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
