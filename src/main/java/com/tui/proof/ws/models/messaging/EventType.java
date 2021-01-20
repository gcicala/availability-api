/**
 * 
 */
package com.tui.proof.ws.models.messaging;

import com.tui.proof.ws.messaging.channel.ChannelType;

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
	FIND_BOOKING(ChannelType.FIND_CHANNEL),

	CREATE_BOOKING(ChannelType.CREATE_CHANNEL),

	UPDATE_BOOKING(ChannelType.UPDATE_CHANNEL),

	UPDATE_BOOKING_FLIGHT(ChannelType.UPDATE_CHANNEL),

	DELETE_BOOKING(ChannelType.DELETE_CHANNEL),

	CONFIRMATION_BOOKING(ChannelType.CONFIRMATION_CHANNEL),

	AVAILABILITY(ChannelType.FIND_CHANNEL);

	private ChannelType channelType;

	private EventType(
		ChannelType channelType) {
		this.channelType = channelType;
	}

}
