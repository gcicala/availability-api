package com.tui.proof.ws.controller;

import static com.tui.proof.ws.utils.JsonUtil.formatAsJsonString;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tui.proof.ws.exception.ServiceException;
import com.tui.proof.ws.messaging.event.BookingAvailabilityEvent;
import com.tui.proof.ws.messaging.event.BookingCreateEvent;
import com.tui.proof.ws.messaging.event.BookingFlightUpdateEvent;
import com.tui.proof.ws.messaging.event.BookingUpdateEvent;
import com.tui.proof.ws.models.web.Response;
import com.tui.proof.ws.services.BookingService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api")
@CrossOrigin
@Validated
@OpenAPIDefinition(tags = { @Tag(name = "Methods", description = "Controller exposing operations from availability-api channel.") }, info = @Info(title = "availability-api", version = "1.0"))
public class FooController {

	private BookingService bookingService;

	@Autowired
	public FooController(
		BookingService bookingService) {
		this.bookingService = bookingService;

	}

	@RequestMapping(method = RequestMethod.POST, value = "/availability")
	public Response<?> checkAvailability(
			@RequestBody @Valid BookingAvailabilityEvent availabilityRequest) throws ServiceException {
		final String methodName = "checkAvailability";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " request body [ {} ] ", "[" + formatAsJsonString(availabilityRequest) + "]");
		}
		Response<?> res = bookingService.checkAvailability(availabilityRequest);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Response body [ {} ] ", "[" + formatAsJsonString(res) + "]");
		}

		return res;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/booking/{bookingId}")
	public Response<?> bookingFind(
			@PathVariable @Valid String bookingId) throws ServiceException {
		final String methodName = "bookingFind";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " request param bookingId [ {} ] ", "[" + bookingId + "]");
		}

		Response<?> res = bookingService.bookingFind(bookingId);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Response body [ {} ] ", "[" + formatAsJsonString(res) + "]");
		}

		return res;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/booking")
	public Response<?> bookingCreate(
			@RequestBody @Valid BookingCreateEvent booking) throws ServiceException {
		final String methodName = "bookingCreate";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " request body [ {} ] ", "[" + formatAsJsonString(booking) + "]");
		}
		Response<?> res = bookingService.bookingCreate(booking);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Response body [ {} ] ", "[" + formatAsJsonString(res) + "]");
		}

		return res;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/booking/{bookingId}")
	public Response<?> bookingUpdate(
			@RequestBody @Valid BookingUpdateEvent bookingBody,
			@PathVariable @Valid String bookingId) throws ServiceException {
		final String methodName = "bookingUpdate";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " request param bookingId [ {} ] body [ {} ] ", "[" + bookingId + "]", "[" + formatAsJsonString(bookingBody) + "]");
		}

		Response<?> res = bookingService.bookingUpdate(bookingBody, bookingId);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Response<?> body [ {} ] ", "[" + formatAsJsonString(res) + "]");
		}

		return res;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/booking/{bookingId}")
	public Response<?> bookingDelete(
			@PathVariable @Valid String bookingId) throws ServiceException {
		final String methodName = "bookingDelete";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " request param bookingId [ {} ] ", "[" + bookingId + "]");
		}

		Response<?> res = bookingService.bookingDelete(bookingId);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Response<?> body [ {} ] ", "[" + formatAsJsonString(res) + "]");
		}

		return res;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/booking/{bookingId}/flight/{flightNumber}")
	public Response<?> bookingDeleteFlight(
			@PathVariable @Valid String bookingId,
			@PathVariable @Valid String flightNumber) throws ServiceException {
		final String methodName = "bookingDeleteFlight";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " request param bookingId [ {} ] flightNumber [ {} ]", "[" + bookingId + "]", "[" + flightNumber + "]");
		}

		Response<?> res = bookingService.bookingDeleteFlight(bookingId, flightNumber);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Response<?> body [ {} ] ", "[" + formatAsJsonString(res) + "]");
		}

		return res;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/booking/{bookingId}/flight")
	public Response<?> bookingAddFlight(
			@PathVariable @Valid String bookingId,
			@RequestBody @Valid BookingFlightUpdateEvent flightBody) throws ServiceException {
		final String methodName = "bookingUpdate";

		if (log.isDebugEnabled()) {
			log.debug(methodName + " request param bookingId [ {} ] body [ {} ] ", "[" + bookingId + "]", "[" + formatAsJsonString(flightBody) + "]");
		}

		Response<?> res = bookingService.bookingAddFlight(flightBody);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " Response<?> body [ {} ] ", "[" + formatAsJsonString(res) + "]");
		}

		return res;
	}
}
