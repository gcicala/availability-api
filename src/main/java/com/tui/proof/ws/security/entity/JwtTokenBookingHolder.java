/**
 * 
 */
package com.tui.proof.ws.security.entity;

import javax.validation.constraints.NotBlank;

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
public class JwtTokenBookingHolder {

	private String jwtTokenBookingholderId;
	@NotBlank
	private String email;
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private JwtTokenBookingHolderRole[] roles;

}
