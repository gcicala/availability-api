/**
 * 
 */
package com.tui.proof.ws.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tui.proof.ws.security.exception.JwtException;
import com.tui.proof.ws.security.model.entity.JwtTokenUser;
import com.tui.proof.ws.security.model.repository.JwtTokenRepository;

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
public class JwtTokenUserDetailsService<T extends JwtTokenUser> implements UserDetailsService {

	@Autowired
	private JwtTokenRepository<T> jwtTokenBookingHolderRepository;

	@Override
	public UserDetails loadUserByUsername(
			String username) throws UsernameNotFoundException {
		T user;
		try {
			user = jwtTokenBookingHolderRepository.findByUsername(username);
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