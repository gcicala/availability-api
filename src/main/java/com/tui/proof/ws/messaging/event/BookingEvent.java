/**
 * 
 */
package com.tui.proof.ws.messaging.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tui.proof.ws.models.messaging.EventType;
import com.tui.proof.ws.models.messaging.Message;

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
public abstract class BookingEvent<T> implements Message {

	protected String id;

	protected T payload;

	protected EventType eventType;

}