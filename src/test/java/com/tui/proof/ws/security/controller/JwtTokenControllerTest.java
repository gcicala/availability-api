/**
 * 
 */
package com.tui.proof.ws.security.controller;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;

import com.tui.proof.ws.security.engine.JwtTokenProvider;
import com.tui.proof.ws.security.entity.JwtTokenBookingHolderRole;
import com.tui.proof.ws.security.model.web.JwtTokenRequest;
import com.tui.proof.ws.security.model.web.JwtTokenResponse;
import com.tui.proof.ws.security.service.JwtTokenUserDetailsService;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 11-gen-2021
 * 
 * @Date : 11-gen-2021
 * 
 * @Time : 13.48.35
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.controller.JwtTokenControllerTest
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class JwtTokenControllerTest {
	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private JwtTokenUserDetailsService jwtTokenUserDetailsService;

	@Mock
	private Logger log;
	@InjectMocks
	private JwtTokenController jwtTokenController;

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.security.controller.JwtTokenController#createAuthenticationToken(com.tui.proof.ws.security.model.web.JwtTokenRequest)}.
	 */
	@Test
	public void testCreateAuthenticationToken() throws Exception {
		JwtTokenRequest request = new JwtTokenRequest();
		request.setPassword("tuiflyuser");
		request.setUsername("tuiflypassword");
		request.setRoles(Arrays.asList(JwtTokenBookingHolderRole.values()));
		JwtTokenResponse tokenResponse = jwtTokenController.createAuthenticationToken(request).getBody();
		log.debug("Token ", tokenResponse.getJwttoken());
		assertNotNull(tokenResponse);
	}

}
