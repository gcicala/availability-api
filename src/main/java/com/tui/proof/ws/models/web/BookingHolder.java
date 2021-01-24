/**
 * 
 */
package com.tui.proof.ws.models.web;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class BookingHolder implements BookingModel {

	@NotBlank(message = "The name is mandatory")
	@Size(min = 1, max = 15, message = "The name accept min 1 max 15 characters")
	private String name;

	@NotBlank(message = "The lastName is mandatory")
	@Size(min = 1, max = 15, message = "The lastName accept min 1 max 15 characters")
	private String lastName;

	@NotBlank(message = "The address is mandatory")
	@Size(min = 4, max = 50, message = "The address accept min 4 max 50 characters")
	private String address;

	@NotBlank(message = "The postalCode is mandatory")
	@Size(min = 5, max = 5, message = "The postalCode accept 5 characters")
	private String postalCode;

	@NotBlank(message = "The country is mandatory")
	@Size(min = 2, max = 2, message = "The country accept 2 uppercase characters")
	private String country;

	@NotBlank(message = "The email is mandatory")
	@Size(min = 6, max = 20, message = "The email min 6 max 20 characters")
	@Email
	private String email;

	@NotBlank(message = "The origin Airport is mandatory")
	@Size(min = 4, max = 15, message = "The Airport accept min 4 max 15 characters")
	private List<String> telephones;
}
