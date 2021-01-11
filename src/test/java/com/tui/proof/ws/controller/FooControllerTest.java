/**
 * 
 */
package com.tui.proof.ws.controller;

import static com.tui.proof.ws.utils.JsonUtil.loadJSON;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.tui.proof.ws.messaging.event.BookingAvailabilityEvent;
import com.tui.proof.ws.messaging.event.BookingCreateEvent;
import com.tui.proof.ws.messaging.event.BookingEventDispatcher;
import com.tui.proof.ws.messaging.event.BookingUpdateEvent;
import com.tui.proof.ws.models.messaging.EventType;
import com.tui.proof.ws.models.web.AvailabilityResponse;
import com.tui.proof.ws.models.web.Booking;
import com.tui.proof.ws.models.web.BookingAvailability;
import com.tui.proof.ws.models.web.BookingHolder;
import com.tui.proof.ws.models.web.BookingResponse;
import com.tui.proof.ws.models.web.Flight;
import com.tui.proof.ws.models.web.Paxe;
import com.tui.proof.ws.models.web.Response;
import com.tui.proof.ws.services.BookingService;

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
@RunWith(MockitoJUnitRunner.class)
public class FooControllerTest {
	@Mock
	private BookingEventDispatcher bookingEventDispatcher;

	@Mock
	private BookingService bookingService;

	@InjectMocks
	private FooController fooController;

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.controller.FooController#checkAvailability(com.tui.proof.ws.messaging.event.BookingAvailabilityEvent)}.
	 */
	@Test
	public void testCheckAvailability() throws Throwable {

		BookingAvailabilityEvent event = new BookingAvailabilityEvent();
		event.setId("FFAA12");
		event.setEventType(EventType.AVAILABILITY);
		BookingAvailability payload = new BookingAvailability();
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
		List<Flight> flights = Arrays.asList(loadJSON(Paths.get("./src/main/resources/data").toFile(), Flight.class.getSimpleName() + ".json", Flight.class));
		AvailabilityResponse availabilityResponse = new AvailabilityResponse();
		availabilityResponse.setResponse(flights);
		availabilityResponse.setRequestId(event.getId());
		availabilityResponse.setResponseId(UUID.randomUUID().toString());

		Mockito.when(bookingService.checkAvailability(event)).thenReturn(availabilityResponse);

		Response checkAvailability = fooController.checkAvailability(event);
		assertNotNull(checkAvailability);

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

		BookingCreateEvent event = new BookingCreateEvent();
		event.setEventType(EventType.CREATE_BOOKING);
		event.setId("456FGB");

		Booking booking = createBookingMock();

		event.setPayload(booking);

		BookingResponse response = new BookingResponse();
		response.setResponse(booking);
		response.setRequestId(event.getId());
		response.setResponseId(UUID.randomUUID().toString());
		response.setEsito(true);

		Mockito.when(bookingService.bookingCreate(event)).thenReturn(response);
		Response createResponse = fooController.bookingCreate(event);
		assertNotNull(createResponse);
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

		BookingUpdateEvent event = new BookingUpdateEvent();
		event.setEventType(EventType.UPDATE_BOOKING);
		event.setId("456FGB");

		Booking booking = createBookingMock();

		event.setPayload(booking);
		BookingResponse response = new BookingResponse();
		response.setResponse(booking);
		response.setRequestId(event.getId());
		response.setResponseId(UUID.randomUUID().toString());
		response.setEsito(true);

		Mockito.when(bookingService.bookingUpdate(event, "456FGB")).thenReturn(response);

		Response updateResponse = fooController.bookingUpdate(event, "456FGB");
		assertNotNull(updateResponse);
	}

	private final Booking createBookingMock() {
		Booking booking = new Booking();
		booking.setBookingId(UUID.randomUUID().toString());

		BookingHolder holder = new BookingHolder();
		holder.setAddress("Via Roma 12");
		holder.setCountry("IT");
		holder.setEmail("mario.rossi@rossi.it");
		holder.setLastName("Rossi");
		holder.setName("Mario");
		holder.setPostalCode("00100");
		holder.setTelephones(Arrays.asList("3489665214", "3256566265"));
		booking.setHolder(holder);

		Flight flight = new Flight();
		flight.setCompany("QatarAirLines");
		flight.setCurrency(Currency.getInstance("USD"));
		flight.setDate(LocalDate.now().plusMonths(1l));
		flight.setFlightNumber("QA" + ThreadLocalRandom.current().nextInt(1000, 9999));
		flight.setHour(LocalTime.now().minusHours(2l));
		BigDecimal price = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(30.00, 999.99));
		MathContext mc_HALF_DOWN = new MathContext(1, RoundingMode.HALF_DOWN);
		flight.setPrice(price.round(mc_HALF_DOWN));

		List<Flight> flights = Arrays.asList(flight);
		booking.setFlights(flights);
		return booking;
	}

}
