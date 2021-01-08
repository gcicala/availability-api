/**
 * 
 */
package com.tui.proof.ws.services;

import static com.tui.proof.ws.utils.JsonUtil.loadJSON;
import static com.tui.proof.ws.utils.JsonUtil.writeJSON;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tui.proof.ws.exception.ServiceException;
import com.tui.proof.ws.messaging.event.BookingAvailabilityEvent;
import com.tui.proof.ws.messaging.event.BookingCreateEvent;
import com.tui.proof.ws.messaging.event.BookingDeleteEvent;
import com.tui.proof.ws.messaging.event.BookingEvent;
import com.tui.proof.ws.messaging.event.BookingEventDispatcher;
import com.tui.proof.ws.messaging.event.BookingFindEvent;
import com.tui.proof.ws.messaging.event.BookingFlightUpdateEvent;
import com.tui.proof.ws.messaging.event.BookingUpdateEvent;
import com.tui.proof.ws.models.messaging.EventType;
import com.tui.proof.ws.models.web.AvailabilityResponse;
import com.tui.proof.ws.models.web.Booking;
import com.tui.proof.ws.models.web.BookingAvailability;
import com.tui.proof.ws.models.web.BookingHolder;
import com.tui.proof.ws.models.web.BookingResponse;
import com.tui.proof.ws.models.web.Flight;
import com.tui.proof.ws.models.web.Paxe;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 29-dic-2020
 * 
 * @Date : 29-dic-2020
 * 
 * @Time : 17.52.39
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.services.BookingService
 * 
 */
@Service
@Log4j2
public class BookingService {

	private BookingEventDispatcher bookingEventDispatcher;

	@Autowired
	public BookingService(
		BookingEventDispatcher bookingEventDispatcher) {
		this.bookingEventDispatcher = bookingEventDispatcher;
	}

	public AvailabilityResponse checkAvailability(
			BookingAvailabilityEvent availabilityEvent) throws ServiceException {
		try {
			asynchDispatcher(availabilityEvent);
			BookingAvailability bookingAvailability = availabilityEvent.getPayload();
			String originAirport = bookingAvailability.getOriginAirport();
			String destinationAirport = bookingAvailability.getDestinationAirport();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate startDate = bookingAvailability.getStartDate();
			LocalDate endDate = bookingAvailability.getEndDate();
			Paxe paxes = bookingAvailability.getPaxes();

			List<Flight> flights = getFlights().stream().filter(flight -> flight.getDate().compareTo(startDate) >= 0 && flight.getDate().compareTo(endDate) == -1).collect(Collectors.toList());

			AvailabilityResponse result = new AvailabilityResponse();
			result.setResponse(flights);
			result.setRequestId(availabilityEvent.getId());
			result.setResponseId(UUID.randomUUID().toString());
			return result;
		} catch (Throwable e) {
			ServiceException thrown = null;
			if (e instanceof ServiceException) {
				thrown = (ServiceException) e;
			} else {
				thrown = new ServiceException(e.getMessage(), e.getMessage());
			}
			throw thrown;
		}

	}

	public BookingResponse bookingUpdate(
			BookingUpdateEvent bookingBody,
			String bookingId) throws ServiceException {
		try {
			bookingBody.setId(bookingId);
			asynchDispatcher(bookingBody);

			BookingResponse result = new BookingResponse();
			result.setRequestId(bookingBody.getId());
			result.setResponseId(UUID.randomUUID().toString());
			result.setEsito(true);
			Booking response = findBookingById(bookingId);
			bookingBody.getPayload().setBookingId(bookingId);
			response = bookingBody.getPayload();
			updateBooking(bookingBody.getPayload());
			result.setResponse(response);

			return result;
		} catch (Throwable e) {
			ServiceException thrown = null;
			if (e instanceof ServiceException) {
				thrown = (ServiceException) e;
			} else {
				thrown = new ServiceException(e.getMessage(), e.getMessage());
			}
			throw thrown;
		}
	}

	public BookingResponse bookingDelete(
			String bookingId) throws ServiceException {
		try {
			LinkedHashMap<String, String> payload = new LinkedHashMap<String, String>();
			payload.put("bookingId", bookingId);
			BookingDeleteEvent bookingEvent = new BookingDeleteEvent();
			bookingEvent.setEventType(EventType.DELETE_BOOKING);
			bookingEvent.setId(bookingId);
			bookingEvent.setPayload(payload);
			asynchDispatcher(bookingEvent);

			BookingResponse result = new BookingResponse();
			result.setEsito(existBookingId(bookingId));
			if (existBookingId(bookingId)) {
				result.setResponse(findBookingById(bookingId));
			}
			deleteBookingById(bookingId);
			result.setRequestId(bookingId);
			result.setResponseId(UUID.randomUUID().toString());

			return result;
		} catch (Throwable e) {
			ServiceException thrown = null;
			if (e instanceof ServiceException) {
				thrown = (ServiceException) e;
			} else {
				thrown = new ServiceException(e.getMessage(), e.getMessage());
			}
			throw thrown;
		}
	}

	public BookingResponse bookingCreate(
			BookingCreateEvent bookingBody) throws ServiceException {
		try {
			asynchDispatcher(bookingBody);
			List<Booking> bookings = getBookings();

			BookingResponse result = new BookingResponse();
			result.setRequestId(bookingBody.getId());
			result.setResponseId(UUID.randomUUID().toString());
			result.setEsito(true);

			Booking response = bookingBody.getPayload();
			response.setBookingId(UUID.randomUUID().toString());

			BookingHolder holder = response.getHolder();

			List<Flight> flights = response.getFlights();

			response.setFlights(flights);

			bookings.add(response);

			writeBookings(bookings);

			result.setResponse(response);

			return result;
		} catch (Throwable e) {
			ServiceException thrown = null;
			if (e instanceof ServiceException) {
				thrown = (ServiceException) e;
			} else {
				thrown = new ServiceException(e.getMessage(), e.getMessage());
			}
			throw thrown;
		}
	}

	public BookingResponse bookingFind(
			String bookingId) throws ServiceException {
		try {
			LinkedHashMap<String, String> payload = new LinkedHashMap<String, String>();
			payload.put("bookingId", bookingId);
			BookingFindEvent bookingEvent = new BookingFindEvent();
			bookingEvent.setEventType(EventType.FIND_BOOKING);
			bookingEvent.setId(bookingId);
			bookingEvent.setPayload(payload);
			asynchDispatcher(bookingEvent);

			BookingResponse result = new BookingResponse();
			result.setRequestId(bookingEvent.getId());
			result.setResponseId(UUID.randomUUID().toString());
			result.setEsito(true);

			Booking response = findBookingById(bookingId);
			result.setResponse(response);

			return result;
		} catch (Throwable e) {
			ServiceException thrown = null;
			if (e instanceof ServiceException) {
				thrown = (ServiceException) e;
			} else {
				thrown = new ServiceException(e.getMessage(), e.getMessage());
			}
			throw thrown;
		}
	}

	public BookingResponse bookingAddFlight(
			BookingFlightUpdateEvent lightUpdateEvent) throws ServiceException {
		try {

			asynchDispatcher(lightUpdateEvent);

			Booking response = findBookingById(lightUpdateEvent.getBookingId());
			Flight flight = lightUpdateEvent.getPayload();
			if (!response.getFlights().stream().anyMatch(fligh -> fligh.getFlightNumber().equals(flight.getFlightNumber()))) {
				response.getFlights().add(flight);
				updateBooking(response);
			}
			BookingResponse result = new BookingResponse();
			result.setRequestId(lightUpdateEvent.getId());
			result.setResponseId(UUID.randomUUID().toString());
			result.setEsito(true);

			result.setResponse(response);

			return result;
		} catch (Throwable e) {
			ServiceException thrown = null;
			if (e instanceof ServiceException) {
				thrown = (ServiceException) e;
			} else {
				thrown = new ServiceException(e.getMessage(), e.getMessage());
			}
			throw thrown;
		}
	}

	public BookingResponse bookingDeleteFlight(
			String bookingId,
			String flightNumber) throws ServiceException {
		try {
			LinkedHashMap<String, String> payload = new LinkedHashMap<String, String>();
			payload.put("bookingId", bookingId);
			BookingDeleteEvent bookingEvent = new BookingDeleteEvent();
			bookingEvent.setEventType(EventType.DELETE_BOOKING);
			bookingEvent.setId(bookingId);
			bookingEvent.setPayload(payload);
			asynchDispatcher(bookingEvent);

			BookingResponse result = new BookingResponse();
			result.setEsito(existFlightNumberInBookingId(flightNumber, bookingId));
			if (existFlightNumberInBookingId(flightNumber, bookingId)) {
				Booking response = findBookingById(bookingId);
				List<Flight> flights = new ArrayList<>();
				flights.addAll(response.getFlights());
				flights.removeIf(flight -> flight.getFlightNumber().equals(flightNumber));
				response.setFlights(flights);
				updateBooking(response);
				result.setResponse(response);
			}
			result.setRequestId(bookingId);
			result.setResponseId(UUID.randomUUID().toString());

			return result;
		} catch (Throwable e) {
			ServiceException thrown = null;
			if (e instanceof ServiceException) {
				thrown = (ServiceException) e;
			} else {
				thrown = new ServiceException(e.getMessage(), e.getMessage());
			}
			throw thrown;
		}
	}

	@Async
	private final void asynchDispatcher(
			BookingEvent<?> bookingEvent) throws ServiceException {
		bookingEventDispatcher.dispatch(bookingEvent);
	}

	@Async
	private final void writeBookings(
			List<Booking> bookings) throws Throwable {
		writeJSON(Paths.get("src/main/resources").toFile(), "Bookings.json", bookings);
	}

	private final List<Flight> getFlights() throws Throwable {
		List<Flight> flights = new ArrayList<Flight>();
		List<Flight> list = Arrays.asList(loadJSON("/List.json", Flight[].class, false));
		flights.addAll(Optional.ofNullable(list).orElse(new ArrayList<Flight>()));

		return flights;
	}

	private final List<Booking> getBookings() throws Throwable {
		List<Booking> bookings = new ArrayList<Booking>();
		List<Booking> list = Arrays.asList(loadJSON("/Bookings.json", Booking[].class, false));
		bookings.addAll(Optional.ofNullable(list).orElse(new ArrayList<Booking>()));

		return bookings;
	}

	private boolean deleteBookingById(
			String bookingId) throws Throwable {
		boolean result = false;
		if (existBookingId(bookingId)) {
			List<Booking> bookings = getBookings();
			bookings.removeIf(booking -> booking.getBookingId().equals(bookingId));
			writeBookings(bookings);
			result = true;
		}

		return result;
	}

	private boolean updateBooking(
			Booking booking) throws Throwable {
		boolean result = false;
		if (existBookingId(booking.getBookingId())) {
			List<Booking> bookings = getBookings();
			bookings.removeIf(book -> book.getBookingId().equals(booking.getBookingId()));
			bookings.add(booking);
			writeBookings(bookings);
			result = true;
		}

		return result;
	}

	private final Booking findBookingById(
			String bookingId) throws Throwable {
		Booking response = getBookings().stream().filter(booking -> booking.getBookingId().equals(bookingId)).findFirst().orElseThrow(() -> new ServiceException("No Booking with id " + bookingId, "INVALID_BOOKING_ID"));
		return response;
	}

	private final boolean existBookingId(
			String bookingId) throws Throwable {
		return getBookings().stream().anyMatch(booking -> booking.getBookingId().equals(bookingId));
	}

	private final boolean existFlightNumberInBookingId(
			String flightNumber,
			String bookingId) throws Throwable {
		return getBookings().stream().filter(booking -> booking.getBookingId().equals(bookingId)).filter(book -> book.getFlights().stream().anyMatch(flight -> flight.getFlightNumber().equals(flightNumber)))
				.anyMatch(booking -> booking.getBookingId().equals(bookingId));
	}

	private final Flight findBookingFlightByBookingIdAndFlighNumber(
			String bookingId,
			String flightNumber) throws Throwable {
		Flight flights = findBookingById(bookingId).getFlights().stream().filter(flight -> !flight.getFlightNumber().equals(flightNumber)).findFirst()
				.orElseThrow(() -> new ServiceException("No Flight with flightNumber " + flightNumber, "INVALID_FLIGHTNUMBER"));
		return flights;
	}

}
