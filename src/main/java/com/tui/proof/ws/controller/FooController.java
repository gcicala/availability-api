package com.tui.proof.ws.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tui.proof.ws.exception.ServiceException;
import com.tui.proof.ws.messaging.event.BookingAvailabilityEvent;
import com.tui.proof.ws.messaging.event.BookingCreateEvent;
import com.tui.proof.ws.messaging.event.BookingUpdateEvent;
import com.tui.proof.ws.models.web.AvailabilityResponse;
import com.tui.proof.ws.models.web.Booking;
import com.tui.proof.ws.models.web.Response;
import com.tui.proof.ws.services.BookingService;
import com.tui.proof.ws.utils.Utils;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api")
@CrossOrigin
@OpenAPIDefinition(tags = { @Tag(name = "Methods", description = "Controller exposing operations from availability-api channel.") }, info = @Info(title = "availability-api", version = "1.0"))
public class FooController {

	private BookingService bookingService;

	private Utils utils;

	@Autowired
	public FooController(
		Utils utils,
		BookingService bookingService) {
		this.utils = utils;
		this.bookingService = bookingService;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/availability")
	public AvailabilityResponse checkAvailability(
			@RequestBody @Valid BookingAvailabilityEvent availabilityRequest) throws ServiceException {
		final String methodName = "checkAvailability";

		if (log.isDebugEnabled()) {
			log.debug(methodName);
		}
		AvailabilityResponse res = bookingService.checkAvailability(availabilityRequest);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " response body [ {} ] ", "[" + utils.formatAsJsonString(res) + "]");
		}

		return res;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/booking/{bookingId}")
	public Response bookingFind(
			@PathVariable @Valid String bookingId) throws ServiceException {
		final String methodName = "bookingFind";

		if (log.isDebugEnabled()) {
			log.debug(methodName);
		}

		Response res = bookingService.bookingFind(bookingId);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " response body [ {} ] ", "[" + utils.formatAsJsonString(res) + "]");
		}

		return res;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/booking")
	public Response bookingCreate(
			@RequestBody @Valid BookingCreateEvent booking) throws ServiceException {
		final String methodName = "bookingCreate";

		if (log.isDebugEnabled()) {
			log.debug(methodName);
		}
		Response res = bookingService.bookingCreate(booking);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " response body [ {} ] ", "[" + utils.formatAsJsonString(res) + "]");
		}

		return res;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/booking/{bookingId}")
	public Response bookingDelete(
			@PathVariable @Valid String bookingId) throws ServiceException {
		final String methodName = "bookingDelete";

		if (log.isDebugEnabled()) {
			log.debug(methodName);
		}

		Response res = bookingService.bookingDelete(bookingId);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " response body [ {} ] ", "[" + utils.formatAsJsonString(res) + "]");
		}

		return res;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/booking/{bookingId}")
	public Response bookingUpdate(
			@RequestBody @Valid BookingUpdateEvent bookingBody,
			@PathVariable @Valid String bookingId) throws ServiceException {
		final String methodName = "bookingUpdate";

		if (log.isDebugEnabled()) {
			log.debug(methodName);
		}

		Response res = bookingService.bookingUpdate(bookingBody, bookingId);

		if (log.isDebugEnabled()) {
			log.debug(methodName + " response body [ {} ] ", "[" + utils.formatAsJsonString(res) + "]");
		}

		return res;
	}
}
