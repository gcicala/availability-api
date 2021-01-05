/**
 * 
 */
package com.tui.proof.ws.models.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 2-gen-2021
 * 
 * @Date : 2-gen-2021
 * 
 * @Time : 17.31.22
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.web.BookingResponse
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponse extends Response<Booking> {

	public boolean esito;
}
