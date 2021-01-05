/**
 * 
 */
package com.tui.proof.ws.messaging.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tui.proof.ws.exception.ServiceException;
import com.tui.proof.ws.models.messaging.Channel;
import com.tui.proof.ws.models.messaging.DynamicRouter;
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
 * @Time : 18.25.36
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.messaging.BookingEventDispatcher
 * 
 */
@Component
@Log4j2
public class BookingEventDispatcher implements DynamicRouter<BookingEvent<?>> {

	private Map<EventType, Channel> handlers = new HashMap<EventType, Channel>();

	@Autowired
	public BookingEventDispatcher(
		List<? extends Channel> eventList) {
		for (Channel channel : eventList) {
			log.debug("Channel Class {} EventType {} EventClass {}", channel.getClass(), channel.getEventType());
			this.handlers.put(channel.getEventType(), channel);
		}

	}

	@Override
	public void dispatch(
			BookingEvent<?> content) throws ServiceException {
		Optional.of(handlers.get(content.getEventType())).orElseThrow(() -> new ServiceException("No Channel for " + content.getEventType().name(), "INVALID_HANDLER")).dispatch(content);

	}
}