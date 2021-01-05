/**
 * 
 */
package com.tui.proof.ws.models.messaging;

import com.tui.proof.ws.models.web.Response;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 29-dic-2020
 * 
 * @Date : 29-dic-2020
 * 
 * @Time : 18.20.17
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.messaging.Channel
 * 
 */
public interface Channel<E extends Message> {

	public EventType getEventType();

	public void dispatch(
			E message);
}
