/**
 * 
 */
package com.tui.proof.ws.messaging.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tui.proof.ws.models.web.Flight;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 3-gen-2021
 * 
 * @Date : 3-gen-2021
 * 
 * @Time : 22.40.18
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.messaging.event.BookingFlightUpdateEvent
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingFlightUpdateEvent extends BookingEvent<Flight> {

	private String bookingId;

}
