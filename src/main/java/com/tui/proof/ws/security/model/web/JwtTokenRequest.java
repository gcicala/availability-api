/**
 * 
 */
package com.tui.proof.ws.security.model.web;

import java.io.Serializable;
import java.util.List;

import com.tui.proof.ws.security.entity.JwtTokenBookingHolderRole;

import lombok.Data;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 19.57.07
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.JwtTokenRequest
 * 
 */
@Data
public class JwtTokenRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	private String username;

	private String password;

	private List<JwtTokenBookingHolderRole> roles;
}
