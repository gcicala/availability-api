/**
 * 
 */
package com.tui.proof.ws.security.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tui.proof.ws.security.exception.JwtException;
import com.tui.proof.ws.security.model.entity.JwtTokenUser;
import com.tui.proof.ws.security.model.repository.JwtTokenRepository;

import lombok.Data;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 12-gen-2021
 * 
 * @Date : 12-gen-2021
 * 
 * @Time : 11.43.07
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.model.service.JwtTokenUserDetails
 * 
 */
@Data
public abstract class JwtTokenUserDetails<T extends JwtTokenUser<?>> implements UserDetailsService {

	@Autowired
	private JwtTokenRepository<T> jwtTokenRepository;

	@Override
	public UserDetails loadUserByUsername(
			String username) throws UsernameNotFoundException {
		T user;
		try {
			user = getJwtTokenRepository().findByUsername(username);
			if (user == null) {
				throw new UsernameNotFoundException("User '" + username + "' not found");
			}
			return org.springframework.security.core.userdetails.User//
					.withUsername(username)//
					.password(user.getPassword())//
					.authorities(user.getRoles())//
					.accountExpired(false)//
					.accountLocked(false)//
					.credentialsExpired(false)//
					.disabled(false)//
					.build();
		} catch (JwtException e) {
			throw new UsernameNotFoundException(e.getMessage(), e);
		}

	}

}
