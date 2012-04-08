package com.wizecom.model;

public class User {
	
	private String id;
	
	private String headers;
	
	private int counter;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", headers=" + headers + ", counter=" + counter + "]";
	}

}
