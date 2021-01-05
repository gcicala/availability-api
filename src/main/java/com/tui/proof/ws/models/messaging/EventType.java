/**
 * 
 */
package com.tui.proof.ws.models.messaging;

import lombok.Getter;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 29-dic-2020
 * 
 * @Date : 29-dic-2020
 * 
 * @Time : 19.11.28
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.messaging.EventType
 * 
 */
@Getter
public enum EventType {
	FIND_BOOKING, 
	
	CREATE_BOOKING, 
	
	UPDATE_BOOKING, 
	
	DELETE_BOOKING, 
	
	AVAILABILITY;

}
