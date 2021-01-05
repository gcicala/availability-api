/**
 * 
 */
package com.tui.proof.ws.services;

import static com.tui.proof.ws.utils.Utils.loadResponse;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.tui.proof.ws.messaging.event.BookingAvailabilityEvent;
import com.tui.proof.ws.messaging.event.BookingCreateEvent;
import com.tui.proof.ws.messaging.event.BookingEventDispatcher;
import com.tui.proof.ws.messaging.event.BookingUpdateEvent;
import com.tui.proof.ws.models.messaging.EventType;
import com.tui.proof.ws.models.web.AvailabilityRequest;
import com.tui.proof.ws.models.web.AvailabilityResponse;
import com.tui.proof.ws.models.web.Booking;
import com.tui.proof.ws.models.web.BookingResponse;
import com.tui.proof.ws.models.web.Flight;
import com.tui.proof.ws.models.web.Paxe;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 4-gen-2021
 * 
 * @Date : 4-gen-2021
 * 
 * @Time : 0.03.38
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.services.BookingServiceTest
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {
	@Mock
	private BookingEventDispatcher bookingEventDispatcher;
	@InjectMocks
	private BookingService bookingService;

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.services.BookingService#checkAvailability(com.tui.proof.ws.messaging.event.BookingAvailabilityEvent)}.
	 */
	@Test
	public void testCheckAvailability() throws Throwable {
		BookingAvailabilityEvent event = new BookingAvailabilityEvent();
		event.setId("FFAA12");
		event.setEventType(EventType.AVAILABILITY);
		AvailabilityRequest payload = new AvailabilityRequest();
		payload.setDestinationAirport("Amsterdam");
		payload.setEndDate(LocalDate.now().plusMonths(2l));
		payload.setOriginAirport("Rome");
		payload.setStartDate(LocalDate.now().plusMonths(1l));
		Paxe paxes = new Paxe();
		paxes.setAdults(1);
		paxes.setChildren(0);
		paxes.setInfants(0);
		payload.setPaxes(paxes);
		event.setPayload(payload);

		Flight flight = new Flight();
		AvailabilityResponse checkAvailability = bookingService.checkAvailability(event);
		assertNotNull(checkAvailability);
	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.services.BookingService#bookingUpdate(com.tui.proof.ws.messaging.event.BookingUpdateEvent, java.lang.String)}.
	 */
	@Test
	public void testBookingUpdate() throws Throwable {
		BookingUpdateEvent event = new BookingUpdateEvent();
		event.setEventType(EventType.UPDATE_BOOKING);
		event.setId("456FGB");

		Booking response = loadResponse("/" + Booking.class.getSimpleName() + ".json", Booking.class, true);
		response.setBookingId(UUID.randomUUID().toString());
		event.setPayload(response);

		BookingResponse updateResponse = bookingService.bookingUpdate(event, "456FGB");
		assertNotNull(updateResponse);
	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.services.BookingService#bookingDelete(java.lang.String)}.
	 */
	@Test
	public void testBookingDelete() throws Throwable {

		BookingResponse createResponse = bookingService.bookingDelete("456FGB");
		assertNotNull(createResponse);

	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.services.BookingService#bookingCreate(com.tui.proof.ws.messaging.event.BookingCreateEvent)}.
	 */
	@Test
	public void testBookingCreate() throws Throwable {
		BookingCreateEvent event = new BookingCreateEvent();
		event.setEventType(EventType.CREATE_BOOKING);
		event.setId("456FGB");

		Booking response = loadResponse("/" + Booking.class.getSimpleName() + ".json", Booking.class, true);
		response.setBookingId(UUID.randomUUID().toString());
		event.setPayload(response);

		BookingResponse createResponse = bookingService.bookingCreate(event);
		assertNotNull(createResponse);
	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.services.BookingService#bookingFind(java.lang.String)}.
	 */
	@Test
	public void testBookingFind() throws Throwable {

		BookingResponse updateResponse = bookingService.bookingFind("456FGB");
		assertNotNull(updateResponse);

	}

}
