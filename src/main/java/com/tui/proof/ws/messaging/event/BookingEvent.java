/**
 * 
 */
package com.tui.proof.ws.messaging.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tui.proof.ws.models.messaging.EventType;
import com.tui.proof.ws.models.messaging.Message;
import com.tui.proof.ws.models.web.BookingModel;

import lombok.Data;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 29-dic-2020
 * 
 * @Date : 29-dic-2020
 * 
 * @Time : 18.24.04
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.messaging.Event
 * 
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingEvent<T extends BookingModel> implements Message {

	private String id;

	private T payload;

	private EventType eventType;

	public <V extends BookingEvent<T>> V createBookingEvent(
			EventType eventType,
			T payload) {
		this.id = UUID.randomUUID().toString();
		this.payload = payload;
		this.eventType = eventType;

		return (V) this;

	}

}