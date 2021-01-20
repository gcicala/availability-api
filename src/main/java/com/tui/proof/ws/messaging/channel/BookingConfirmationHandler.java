/**
 * 
 */
package com.tui.proof.ws.messaging.channel;

import org.springframework.stereotype.Component;

import com.tui.proof.ws.exception.ServiceException;
import com.tui.proof.ws.messaging.event.BookingAvailabilityEvent;
import com.tui.proof.ws.messaging.event.BookingEvent;
import com.tui.proof.ws.messaging.event.BookingFindEvent;
import com.tui.proof.ws.models.messaging.Channel;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 29-dic-2020
 * 
 * @Date : 29-dic-2020
 * 
 * @Time : 18.24.41
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.messaging.BookingConfirmationHandler
 * 
 */
@Component
@Log4j2
public class BookingConfirmationHandler implements Channel<BookingEvent<?>> {

	@Override
	public ChannelType getChannelType() {
		return ChannelType.CONFIRMATION_CHANNEL;
	}

	@Override
	public void dispatch(
			BookingEvent<?> message) throws ServiceException {

		log.debug("Message event {} ChannelType {} payload {}", message.getEventType().name(), message.getEventType().getChannelType().name(), message.getPayload());
		switch (message.getEventType()) {
		case AVAILABILITY:
			BookingAvailabilityEvent availabilityEvent = (BookingAvailabilityEvent) message;
			break;
		case FIND_BOOKING:
			BookingFindEvent findEvent = (BookingFindEvent) message;
			break;
		default:
			break;
		}
	}
}