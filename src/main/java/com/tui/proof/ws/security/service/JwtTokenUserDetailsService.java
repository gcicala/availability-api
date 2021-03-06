/**
 * 
 */
package com.tui.proof.ws.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tui.proof.ws.security.entity.JwtTokenBookingHolder;
import com.tui.proof.ws.security.exception.JwtException;
import com.tui.proof.ws.security.model.service.JwtTokenUserDetails;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 11.40.51
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.UserDetails
 * 
 */
@Service
public class JwtTokenUserDetailsService extends JwtTokenUserDetails<JwtTokenBookingHolder> {

	@Override
	public UserDetails loadUserByUsername(
			String username) throws UsernameNotFoundException {
		JwtTokenBookingHolder user;
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