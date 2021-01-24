/**
 * 
 */
package com.tui.proof.ws.controller;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.tui.proof.ws.exception.ServiceException;
import com.tui.proof.ws.models.web.error.ErrorResponse;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 0.03.26
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.controller.ControllerErrorAdvisor
 * 
 */
@ControllerAdvice
@Log4j2
public class ControllerErrorAdvisor {

	@ExceptionHandler(UndeclaredThrowableException.class)
	protected ResponseEntity<ErrorResponse> handleUndeclaredThrowable(
			UndeclaredThrowableException ex,
			WebRequest request,
			HttpServletRequest req) {
		log.error("An error occured " + ex.getUndeclaredThrowable().getMessage(), ex.getUndeclaredThrowable());
		ErrorResponse er = new ErrorResponse("ERR_404_BAD_REQUEST", ex.getUndeclaredThrowable().getMessage());
		log.info("ErrorResponse [ " + er + " ]");
		return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
	}

	@ResponseBody
	@ExceptionHandler(ServiceException.class)
	protected ResponseEntity<ErrorResponse> serviceExceptionHandler(
			ServiceException ex) {
		ErrorResponse resp = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
		return new ResponseEntity<ErrorResponse>(resp, ex.getErrorStatus());
	}

	@ResponseBody
	@ExceptionHandler(BadCredentialsException.class)
	protected ResponseEntity<ErrorResponse> serviceBadCredentialsExceptionHandler(
			BadCredentialsException ex) {
		ErrorResponse resp = new ErrorResponse("BAD_CREDENTIAL", ex.getMessage());
		return new ResponseEntity<ErrorResponse>(resp, HttpStatus.BAD_REQUEST);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((
				error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}
