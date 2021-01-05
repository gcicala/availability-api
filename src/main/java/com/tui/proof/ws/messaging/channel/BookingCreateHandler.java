/**
 * 
 */
package com.tui.proof.ws.messaging.channel;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.tui.proof.ws.messaging.event.BookingCreateEvent;
import com.tui.proof.ws.models.messaging.Channel;
import com.tui.proof.ws.models.messaging.EventType;

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
public class BookingCreateHandler implements Channel<BookingCreateEvent> {

	@Override
	public EventType getEventType() {
		return EventType.CREATE_BOOKING;
	}

	@Override
	public void dispatch(
			BookingCreateEvent message) {
		log.debug("Message {} payload {}", getEventType().name(), message.getPayload());
	}
}