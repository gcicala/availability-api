/**
 * 
 */
package com.tui.proof.ws.security.web;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tui.proof.ws.security.engine.JwtTokenProvider;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 10.53.42
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.JwtTokenFilterConfigurer
 * 
 */
@EnableWebSecurity
public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private JwtTokenProvider jwtTokenProvider;

	public JwtTokenFilterConfigurer(
		JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void configure(
			HttpSecurity http) throws Exception {
		JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
		http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
	}

}