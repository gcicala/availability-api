/**
 * 
 */
package com.tui.proof.ws.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tui.proof.MainApplication;
import com.tui.proof.ws.exception.ServiceException;
import com.tui.proof.ws.messaging.event.BookingAvailabilityEvent;
import com.tui.proof.ws.messaging.event.BookingCreateEvent;
import com.tui.proof.ws.messaging.event.BookingUpdateEvent;
import com.tui.proof.ws.models.messaging.EventType;
import com.tui.proof.ws.models.web.AvailabilityResponse;
import com.tui.proof.ws.models.web.Booking;
import com.tui.proof.ws.models.web.BookingAvailability;
import com.tui.proof.ws.models.web.BookingHolder;
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
//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class BookingServiceTest {
	// @Mock

	// private BookingEventDispatcher bookingEventDispatcher;

	// @Mock
	// private Path repositoryPath = Paths.get("src/main/resources/data");

	@Autowired
	private BookingService bookingService;

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.services.BookingService#checkAvailability(com.tui.proof.ws.messaging.event.BookingAvailabilityEvent)}.
	 */
	@Test
	public void testBookingCheckAvailability() throws Throwable {
		BookingAvailabilityEvent event = new BookingAvailabilityEvent();
		event.setId("7a3aede9-d8c5-4a7f-96e7-07479f9b9ebb");
		event.setEventType(EventType.AVAILABILITY);
		BookingAvailability payload = new BookingAvailability();
		payload.setDestinationAirport("Amsterdam");
		payload.setEndDate(LocalDate.now().plusMonths(2l));
		payload.setOriginAirport("Rome");
		payload.setStartDate(LocalDate.now().plusMonths(1l));
		payload.setCurrency(Currency.getInstance("EUR"));
		Paxe paxes = new Paxe();
		paxes.setAdults(1);
		paxes.setChildren(0);
		paxes.setInfants(0);
		payload.setPaxes(paxes);
		event.setPayload(payload);

		AvailabilityResponse checkAvailability = bookingService.checkAvailability(payload);
		assertNotNull(checkAvailability);
		assertNotNull(checkAvailability.getResponse());
	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.services.BookingService#bookingCreate(com.tui.proof.ws.messaging.event.BookingCreateEvent)}.
	 */
	@Test
	public void testBookingCreate() throws Throwable {
		try {
			BookingCreateEvent event = new BookingCreateEvent();
			event.setEventType(EventType.CREATE_BOOKING);
			event.setId("85992b8a-e418-45d4-b65f-9b6dfe428bc7");
			Booking booking = createBookingMock();
			event.setPayload(booking);
			BookingResponse createResponse = bookingService.bookingCreate(booking);

			assertNotNull(createResponse);
			assertTrue(createResponse.esito);
			assertTrue(!createResponse.getResponse().isConfirmed());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.services.BookingService#bookingFind(java.lang.String)}.
	 */
	@Test
	public void testBookingFind() throws Throwable {

		BookingResponse updateResponse = bookingService.bookingFind("991a83fc-c434-40a9-8b81-096b3fc60854");
		assertNotNull(updateResponse);
		assertTrue(updateResponse.isEsito());

	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.services.BookingService#bookingUpdate(com.tui.proof.ws.messaging.event.BookingUpdateEvent, java.lang.String)}.
	 */
	@Test
	public void testBookingUpdate() throws Throwable {
		try {
			BookingUpdateEvent event = new BookingUpdateEvent();
			event.setEventType(EventType.UPDATE_BOOKING);
			event.setId("7a3aede9-d8c5-4a7f-96e7-07479f9b9ebb");

			Booking booking = createBookingMock();

			event.setPayload(booking);

			BookingResponse updateResponse = bookingService.bookingUpdate(booking, "7a3aede9-d8c5-4a7f-96e7-07479f9b9ebb");
			assertNotNull(updateResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link com.tui.proof.ws.services.BookingService#bookingDelete(java.lang.String)}.
	 */
	@Test
	public void testBookingDelete() throws Throwable {

		BookingResponse createResponse = bookingService.bookingDelete("85992b8a-e418-45d4-b65f-9b6dfe428bc7");
		assertNotNull(createResponse);

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

	@Test
	public void testBookingConfirmation() {
		try {
			BookingResponse bookingConfirmation = bookingService.bookingConfirmation("a9233217-8ee2-403d-ad89-433c2c3a0f14");
			assertTrue(bookingConfirmation.getResponse().isConfirmed());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
