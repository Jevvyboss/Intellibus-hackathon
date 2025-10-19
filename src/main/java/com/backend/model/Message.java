package com.backend.model;

public class Message {
	private int userId;
	private String message;
	
	public Message() {
		this.userId =0; 
		this.message = "";
	}
	

	public Message(int userId, String message) {
		this.userId = userId;
		this.message = message;
	}
	
	


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	@Override
	public String toString() {
		return "Message [userId=" + userId + ", message=" + message + "]";
	}


	
	
	
	
	

}
