/**
 * 
 */
package com.tui.proof.ws.security.controller;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tui.proof.MainApplication;
import com.tui.proof.ws.security.entity.JwtTokenBookingHolderRole;
import com.tui.proof.ws.security.model.web.JwtTokenRequest;
import com.tui.proof.ws.security.model.web.JwtTokenResponse;

import lombok.extern.log4j.Log4j2;

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
//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
@Log4j2
public class JwtTokenControllerTest {
//	@Mock
//	private AuthenticationManager authenticationManager;
//
//	@Mock
//	private JwtTokenProvider jwtTokenProvider;
//
//	@Mock
//	private JwtTokenUserDetailsService jwtTokenUserDetailsService;
//
//	@Mock
//	private Logger log;

	@Autowired
	private JwtTokenController jwtTokenController;

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.security.controller.JwtTokenController#createAuthenticationToken(com.tui.proof.ws.security.model.web.JwtTokenRequest)}.
	 */
	@Test
	public void testCreateAuthenticationToken() throws Exception {
		JwtTokenRequest request = new JwtTokenRequest();
		request.setPassword("tuiflypassword");
		request.setUsername("tuiflyuser");
		request.setRoles(Arrays.asList(JwtTokenBookingHolderRole.values()));
		JwtTokenResponse tokenResponse = jwtTokenController.createAuthenticationToken(request).getBody();
		log.debug("Token {}", tokenResponse.getJwttoken());
		assertNotNull(tokenResponse);
	}

}
