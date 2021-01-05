/**
 * 
 */
package com.tui.proof.ws.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tui.proof.ws.messaging.event.BookingEventDispatcher;
import com.tui.proof.ws.services.BookingService;
import com.tui.proof.ws.utils.Utils;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 4-gen-2021
 * 
 * @Date : 4-gen-2021
 * 
 * @Time : 0.00.23
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.controller.FooControllerTest
 * 
 */
@ExtendWith(MockitoExtension.class)
public class FooControllerTest {
	@Mock
	private BookingEventDispatcher bookingEventDispatcher;

	@Mock
	private BookingService bookingService;

	@Mock
	private Utils utils;

	@InjectMocks
	private FooController fooController;

	/**
	 * @throws java.lang.Throwable
	 */
	@BeforeEach
	protected void setUp() throws Throwable {
	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.controller.FooController#checkAvailability(com.tui.proof.ws.messaging.event.BookingAvailabilityEvent)}.
	 */
	@Test
	public void testCheckAvailability() throws Throwable {
		
	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.controller.FooController#bookingFind(java.lang.String)}.
	 */
	@Test
	public void testBookingFind() throws Throwable {
		
	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.controller.FooController#bookingCreate(com.tui.proof.ws.messaging.event.BookingCreateEvent)}.
	 */
	@Test
	public void testBookingCreate() throws Throwable {
		
	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.controller.FooController#bookingDelete(java.lang.String)}.
	 */
	@Test
	public void testBookingDelete() throws Throwable {
		
	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.controller.FooController#bookingUpdate(com.tui.proof.ws.messaging.event.BookingUpdateEvent, java.lang.String)}.
	 */
	@Test
	public void testBookingUpdate() throws Throwable {
		
	}

}
