/**
 * 
 */
package com.tui.proof.ws.security.model.web;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 19.59.38
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.JwtResponse
 * 
 */
@Getter
@AllArgsConstructor
public class JwtTokenResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;

	private final String jwttoken;

}
