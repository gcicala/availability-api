/**
 * 
 */
package com.tui.proof.ws.security.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tui.proof.ws.exception.ServiceException;
import com.tui.proof.ws.security.engine.JwtTokenProvider;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 10.50.30
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.JwtTokenFilter
 * 
 */

// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
public class JwtTokenFilter extends OncePerRequestFilter {

	private JwtTokenProvider jwtTokenProvider;

	public JwtTokenFilter(
		JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		String token = jwtTokenProvider.resolveToken(httpServletRequest);
		// try {
		try {
			if (token != null && jwtTokenProvider.validateToken(token)) {
				Authentication auth = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

}