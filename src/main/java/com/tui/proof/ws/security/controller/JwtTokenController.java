/**
 * 
 */
package com.tui.proof.ws.security.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tui.proof.ws.security.engine.JwtTokenProvider;
import com.tui.proof.ws.security.model.service.JwtTokenUserDetails;
import com.tui.proof.ws.security.model.web.JwtTokenRequest;
import com.tui.proof.ws.security.model.web.JwtTokenResponse;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 15.30.31
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.controller.JwtTokenController
 * 
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
@Validated
@OpenAPIDefinition(tags = { @Tag(name = "Methods", description = "Controller exposing operations from availability-api channel.") }, info = @Info(title = "availability-api", version = "1.0"))
public class JwtTokenController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private JwtTokenUserDetails<?> jwtTokenUserDetailsService;

	@RequestMapping(value = "security/authenticate", method = RequestMethod.POST)
	public ResponseEntity<JwtTokenResponse> createAuthenticationToken(
			@RequestBody JwtTokenRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = jwtTokenUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenProvider.createToken(userDetails.getUsername(), authenticationRequest.getRoles());
		return ResponseEntity.ok(new JwtTokenResponse(token));
	}

	@RequestMapping(value = "security/refreshtoken", method = RequestMethod.POST)
	public ResponseEntity<JwtTokenResponse> refreshAuthenticationToken(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody JwtTokenRequest authenticationRequest) throws Throwable {
		String token = jwtTokenProvider.resolveToken(request);
		final String refreshtoken = jwtTokenProvider.refreshToken(token);

		response.addHeader("jwttoken", refreshtoken);
		response.addHeader("authorization", "Bearer " + refreshtoken);

		return ResponseEntity.ok(new JwtTokenResponse(refreshtoken));
	}

	private void authenticate(
			String username,
			String password) throws BadCredentialsException, DisabledException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new DisabledException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", e);
		}
	}

}
