/**
 * 
 */
package com.tui.proof.ws.services;

import static com.tui.proof.ws.utils.JsonUtil.loadJSON;
import static com.tui.proof.ws.utils.JsonUtil.writeJSON;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
import com.tui.proof.ws.models.web.BookingEventParam;
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
@Validated
public class BookingService {

	private BookingEventDispatcher bookingEventDispatcher;

	private Path repositoryPath;

	@Autowired
	public BookingService(
		BookingEventDispatcher bookingEventDispatcher,
		@Value("${security.jwt.token.repository.folder}") Path repositoryPath) {
		this.bookingEventDispatcher = bookingEventDispatcher;
		this.repositoryPath = repositoryPath;
	}

	public AvailabilityResponse checkAvailability(
			@Valid BookingAvailability bookingAvailability) throws ServiceException {
		try {

			BookingAvailabilityEvent event = new BookingAvailabilityEvent().createBookingEvent(EventType.AVAILABILITY, bookingAvailability);
			asynchDispatcher(event);

			String originAirport = bookingAvailability.getOriginAirport();
			String destinationAirport = bookingAvailability.getDestinationAirport();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate startDate = bookingAvailability.getStartDate();
			LocalDate endDate = bookingAvailability.getEndDate();
			Paxe paxes = bookingAvailability.getPaxes();

			List<Flight> flights = getFlights().stream().filter(flight -> flight.getDate().compareTo(startDate) >= 0 && flight.getDate().compareTo(endDate) == -1).collect(Collectors.toList());

			AvailabilityResponse result = new AvailabilityResponse();
			result.setResponse(flights);
			result.setRequestId(event.getId());
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
			Booking bookingBody,
			String bookingId) throws ServiceException {
		try {
			bookingBody.setBookingId(bookingId);
			BookingUpdateEvent event = new BookingUpdateEvent().createBookingEvent(EventType.UPDATE_BOOKING, bookingBody);
			asynchDispatcher(event);

			BookingResponse result = new BookingResponse();
			result.setRequestId(event.getId());
			result.setResponseId(UUID.randomUUID().toString());
			result.setEsito(true);
			Booking response = findBookingById(bookingId);
			bookingBody.setBookingId(bookingId);
			response = bookingBody;
			updateBooking(bookingBody);
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

			BookingEventParam payload = new BookingEventParam();
			payload.setName("bookingId");
			payload.setValue(bookingId);
			BookingDeleteEvent bookingEvent = new BookingDeleteEvent().createBookingEvent(EventType.DELETE_BOOKING, payload);
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
			Booking bookingBody) throws ServiceException {
		try {
			BookingCreateEvent event = new BookingCreateEvent().createBookingEvent(EventType.CREATE_BOOKING, bookingBody);
			asynchDispatcher(event);
			List<Booking> bookings = getBookings();

			BookingResponse result = new BookingResponse();
			result.setRequestId(event.getId());
			result.setResponseId(UUID.randomUUID().toString());
			result.setEsito(true);

			Booking response = bookingBody;
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

	public BookingResponse bookingConfirmation(
			String bookingId) throws ServiceException {
		try {
			BookingEventParam payload = new BookingEventParam();
			payload.setName("bookingId");
			payload.setValue(bookingId);
			BookingDeleteEvent bookingEvent = new BookingDeleteEvent().createBookingEvent(EventType.CONFIRMATION_BOOKING, payload);
			asynchDispatcher(bookingEvent);

			BookingResponse result = new BookingResponse();
			result.setRequestId(bookingId);
			result.setResponseId(UUID.randomUUID().toString());
			result.setEsito(true);
			result.setEsito(existBookingId(bookingId));
			if (existBookingId(bookingId)) {
				Booking booking = findBookingById(bookingId);
				if (booking.isConfirmed()) {
					throw new ServiceException("INVALID_BOOKING_ID", "Booking already confirmed");
				}
				boolean match = booking.getFlights().stream().allMatch(filter -> filter.getDate().compareTo(LocalDate.now()) >= 0 || filter.getHour().compareTo(LocalTime.now().minusMinutes(15)) < 0);
				if (!match) {
					throw new ServiceException("INVALID_FLIGHT_DATE", "Unable to confirm booking the flights is not yet available");
				}
				booking.setConfirmed(match);
				updateBooking(booking);
				result.setResponse(booking);
			}

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
			BookingEventParam payload = new BookingEventParam();
			payload.setName("bookingId");
			payload.setValue(bookingId);
			BookingFindEvent bookingEvent = new BookingFindEvent().createBookingEvent(EventType.FIND_BOOKING, payload);
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
			String bookingId,
			Flight flight) throws ServiceException {
		try {
			BookingFlightUpdateEvent event = new BookingFlightUpdateEvent().createBookingEvent(EventType.UPDATE_BOOKING_FLIGHT, flight);
			event.setBookingId(bookingId);
			asynchDispatcher(event);

			Booking response = findBookingById(bookingId);

			if (!response.getFlights().stream().anyMatch(fligh -> fligh.getFlightNumber().equals(flight.getFlightNumber()))) {
				response.getFlights().add(flight);
				updateBooking(response);
			}
			BookingResponse result = new BookingResponse();
			result.setRequestId(event.getId());
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
			BookingEventParam payload = new BookingEventParam();
			payload.setName("bookingId");
			payload.setValue(bookingId);
			BookingDeleteEvent bookingEvent = new BookingDeleteEvent().createBookingEvent(EventType.DELETE_BOOKING, payload);
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
		writeJSON(repositoryPath.resolve(repositoryPath.toAbsolutePath()).toFile(), "Bookings.json", bookings);
	}

	private final List<Flight> getFlights() throws Throwable {
		List<Flight> flights = new ArrayList<Flight>();
		List<Flight> list = Arrays.asList(loadJSON(repositoryPath.resolve(repositoryPath.toAbsolutePath()).toFile(), "List.json", Flight[].class));
		flights.addAll(Optional.ofNullable(list).orElse(new ArrayList<Flight>()));

		return flights;
	}

	private final List<Booking> getBookings() throws Throwable {
		List<Booking> bookings = new ArrayList<Booking>();
		List<Booking> list = Arrays.asList(loadJSON(repositoryPath.resolve(repositoryPath.toAbsolutePath()).toFile(), "Bookings.json", Booking[].class));
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
