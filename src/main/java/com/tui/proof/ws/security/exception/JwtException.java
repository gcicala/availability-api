/**
 * 
 */
package com.tui.proof.ws.security.exception;

import org.springframework.http.HttpStatus;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 12.23.38
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.exception.JwtException
 * 
 */
public class JwtException extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5654603439488770160L;

	public static final String JWT001_ERR_GENERIC = "JWT001";
	public static final String JWT002_ERR_INVALID_MSISDN = "JWT002";
	public static final String JWT003_ERR_INVALID_SIGNATURE = "JWT003";

	public static final String NOT_VALID_MSISDN = "MSISDN non valido";
	public static final String NOT_SIGNED_JWT = "JWT signature non valido";

	private String outcome;
	private String message;
	private HttpStatus httpStatus = null;

	public JwtException(
		String message) {
		super(message);
		this.message = message;
		this.outcome = "";
	}

	public JwtException(
		String message,
		String outcome) {
		super(message);
		this.outcome = outcome;
		this.message = message;
	}

	public JwtException(
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

		if (httpStatus != null) {
			return httpStatus;
		}
		return HttpStatus.BAD_REQUEST;
	}
}