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
import com.tui.proof.ws.messaging.channel.ChannelType;
import com.tui.proof.ws.models.messaging.Channel;
import com.tui.proof.ws.models.messaging.DynamicRouter;

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

	private Map<ChannelType, Channel<BookingEvent<?>>> handlers = new HashMap<ChannelType, Channel<BookingEvent<?>>>();

	@Autowired
	public BookingEventDispatcher(
		List<? extends Channel<BookingEvent<?>>> eventList) {
		for (Channel<BookingEvent<?>> channel : eventList) {
			log.debug("Channel Class {} ChannelType {} ", channel.getClass(), channel.getChannelType());
			this.handlers.put(channel.getChannelType(), channel);
		}
	}

	@Override
	public void dispatch(
			BookingEvent<?> content) throws ServiceException {
		log.debug("Dispatching Message event {} ChannelType {} payload {}", content.getEventType().name(), content.getEventType().getChannelType().name(), content.getPayload());
		Channel<BookingEvent<?>> channel = Optional.ofNullable(handlers.get(content.getEventType().getChannelType()))
				.orElseThrow(() -> new ServiceException("No Channel " + content.getEventType().getChannelType().name() + " for EventType " + content.getEventType().name(), "INVALID_HANDLER"));
		if (content.getEventType().getChannelType().compareTo(channel.getChannelType()) != 0) {
			throw new ServiceException("Invalid handler request " + content.getEventType().getChannelType() + " and found " + channel.getChannelType().name(), "INVALID_HANDLER");
		}
		channel.dispatch(content);
	}
}