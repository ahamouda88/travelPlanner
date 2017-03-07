package com.travelPlanner.model;

import java.io.Serializable;

public class ErrorResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private int errorCode;
	private String message;

	public ErrorResponse() {
	}

	public ErrorResponse(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + errorCode;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ErrorResponse other = (ErrorResponse) obj;
		if (errorCode != other.errorCode) return false;
		if (message == null) {
			if (other.message != null) return false;
		} else if (!message.equals(other.message)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "ErrorResponse [errorCode=" + errorCode + ", message=" + message + "]";
	}
}
