/**
 * 
 */
package com.tui.proof.ws.messaging.event;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
 * @Class : com.tui.proof.ws.messaging.event.BookingCreateEvent
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingFindEvent extends BookingEvent<LinkedHashMap<String, String>> {

}
