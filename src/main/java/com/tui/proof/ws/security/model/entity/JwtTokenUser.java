/**
 * 
 */
package com.tui.proof.ws.security.model.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 11-gen-2021
 * 
 * @Date : 11-gen-2021
 * 
 * @Time : 14.05.33
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.model.entity.JwtTokenUser
 * 
 */
public interface JwtTokenUser<T extends GrantedAuthority> {

	String getPassword();

	T[] getRoles();
}
