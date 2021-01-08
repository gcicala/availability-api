/**
 * 
 */
package com.tui.proof.ws.models.web;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 2-gen-2021
 * 
 * @Date : 2-gen-2021
 * 
 * @Time : 14.59.13
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.web.BokingHolder
 * 
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingHolder {

	@NotBlank
	private String name;
	@NotBlank
	private String lastName;
	@NotBlank
	private String address;
	@NotBlank
	private String postalCode;
	@NotBlank
	private String country;
	@NotBlank
	private String email;
	@NotEmpty
	private List<String> telephones;
}
