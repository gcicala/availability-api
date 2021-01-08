/**
 * 
 */
package com.tui.proof.ws.models.web;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 29-dic-2020
 * 
 * @Date : 29-dic-2020
 * 
 * @Time : 16.37.07
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.web.Response
 * 
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Response<T> {

	@NotBlank
	private String requestId;

	@NotBlank
	private String responseId;

	@NotNull
	private T response;

}
