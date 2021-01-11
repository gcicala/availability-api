/**
 * 
 */
package com.tui.proof.ws.security.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 13.20.19
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.JwtTokenBookingHolderRole
 * 
 */
public enum JwtTokenBookingHolderRole implements GrantedAuthority {
	ROLE_ADMIN, ROLE_CLIENT;

	@Override
	public String getAuthority() {
		return name();
	}

}