/**
 * 
 */
package com.tui.proof.ws.exception;

import org.springframework.http.HttpStatus;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 29-dic-2020
 * 
 * @Date : 29-dic-2020
 * 
 * @Time : 17.26.58
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.exception.ServiceException
 * 
 */
public class ServiceException extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 117367991490941261L;
	private String message;
	private String outcome;

	private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

	public ServiceException(
		String message,
		String outcome) {
		super(message);
		this.outcome = outcome;
		this.message = message;
	}

	public ServiceException(
		String message,
		String outcome,
		HttpStatus httpStatus) {
		super(message);
		this.outcome = outcome;
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public String getErrorCode() {
		return outcome;
	}

	public String getErrorMessage() {
		return message;
	}

	public HttpStatus getErrorStatus() {
		return httpStatus;
	}

}
