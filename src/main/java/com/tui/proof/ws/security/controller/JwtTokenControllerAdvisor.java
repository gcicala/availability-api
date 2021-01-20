/**
 * 
 */
package com.tui.proof.ws.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tui.proof.ws.models.web.error.ErrorResponse;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 10-gen-2021
 * 
 * @Date : 10-gen-2021
 * 
 * @Time : 20.58.13
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.controller.JwtTokenControllerAdvisor
 * 
 */
@ControllerAdvice
public class JwtTokenControllerAdvisor {

	@ResponseBody
	@ExceptionHandler(DisabledException.class)
	protected ResponseEntity<ErrorResponse> disabledExceptionHandler(
			DisabledException ex) {
		ErrorResponse resp = new ErrorResponse(ex.getMessage(), ex.getMessage());
		return new ResponseEntity<ErrorResponse>(resp, HttpStatus.UNAUTHORIZED);
	}

	@ResponseBody
	@ExceptionHandler(UsernameNotFoundException.class)
	protected ResponseEntity<ErrorResponse> usernameNotFoundExceptionHandler(
			UsernameNotFoundException ex) {
		ErrorResponse resp = new ErrorResponse(ex.getMessage(), ex.getMessage());
		return new ResponseEntity<ErrorResponse>(resp, HttpStatus.UNAUTHORIZED);
	}

	@ResponseBody
	@ExceptionHandler(BadCredentialsException.class)
	protected ResponseEntity<ErrorResponse> badCredentialsExceptionHandler(
			BadCredentialsException ex) {
		ErrorResponse resp = new ErrorResponse(ex.getMessage(), ex.getMessage());
		return new ResponseEntity<ErrorResponse>(resp, HttpStatus.UNAUTHORIZED);
	}

}
