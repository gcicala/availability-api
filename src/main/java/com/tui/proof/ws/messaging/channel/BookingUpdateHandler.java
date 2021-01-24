/**
 * 
 */
package com.tui.proof.ws.messaging.channel;

import org.springframework.stereotype.Component;

import com.tui.proof.ws.messaging.event.BookingEvent;
import com.tui.proof.ws.messaging.event.BookingFlightUpdateEvent;
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
 * @Class : com.tui.proof.ws.models.messaging.Handler
 * 
 */
@Component
@Log4j2
public class BookingUpdateHandler implements Channel<BookingEvent<?>> {

	@Override
	public ChannelType getChannelType() {
		return ChannelType.UPDATE_CHANNEL;
	}

	@Override
	public void dispatch(
			BookingEvent<?> message) {
		log.debug("Message event {} ChannelType {} payload {}", message.getEventType().name(), message.getEventType().getChannelType().name(), message.getPayload());
		switch (message.getEventType()) {
		case UPDATE_BOOKING:
			break;
		case UPDATE_BOOKING_FLIGHT:
			BookingFlightUpdateEvent event = (BookingFlightUpdateEvent) message;
			break;
		default:
			break;
		}

	}

}