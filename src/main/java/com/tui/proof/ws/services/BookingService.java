/**
 * 
 */
package com.tui.proof.ws.services;

import static com.tui.proof.ws.utils.Utils.loadResponse;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.UUID;

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
import com.tui.proof.ws.messaging.event.BookingUpdateEvent;
import com.tui.proof.ws.models.messaging.EventType;
import com.tui.proof.ws.models.web.AvailabilityRequest;
import com.tui.proof.ws.models.web.AvailabilityResponse;
import com.tui.proof.ws.models.web.Booking;
import com.tui.proof.ws.models.web.BookingResponse;
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
			AvailabilityRequest availabilityRequest = availabilityEvent.getPayload();
			String originAirport = availabilityRequest.getOriginAirport();
			String destinationAirport = availabilityRequest.getDestinationAirport();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String startDate = availabilityRequest.getStartDate().format(dateTimeFormatter);
			String endDate = availabilityRequest.getEndDate().format(dateTimeFormatter);
			Paxe paxes = availabilityRequest.getPaxes();

			AvailabilityResponse result = loadResponse("/" + AvailabilityResponse.class.getSimpleName() + ".json", AvailabilityResponse.class, true);
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

			BookingResponse result = loadResponse("/" + BookingResponse.class.getSimpleName() + ".json", BookingResponse.class, true);
			result.setRequestId(bookingBody.getId());
			result.setResponseId(UUID.randomUUID().toString());
			result.setEsito(true);
			Booking response = loadResponse("/" + Booking.class.getSimpleName() + ".json", Booking.class, true);
			response.setBookingId(UUID.randomUUID().toString());
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
			BookingResponse result = loadResponse("/" + BookingResponse.class.getSimpleName() + ".json", BookingResponse.class, true);
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

			BookingResponse result = loadResponse("/" + BookingResponse.class.getSimpleName() + ".json", BookingResponse.class, true);
			result.setRequestId(bookingBody.getId());
			result.setResponseId(UUID.randomUUID().toString());
			result.setEsito(true);
			Booking response = loadResponse("/" + Booking.class.getSimpleName() + ".json", Booking.class, true);
			response.setBookingId(UUID.randomUUID().toString());
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
			BookingResponse result = loadResponse("/" + BookingResponse.class.getSimpleName() + ".json", BookingResponse.class, true);
			result.setRequestId(bookingEvent.getId());
			result.setResponseId(UUID.randomUUID().toString());
			result.setEsito(true);
			Booking response = loadResponse("/" + Booking.class.getSimpleName() + ".json", Booking.class, true);
			response.setBookingId(bookingId);
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

	@Async
	private final void asynchDispatcher(
			BookingEvent<?> bookingEvent) throws ServiceException {
		bookingEventDispatcher.dispatch(bookingEvent);
	}
}
