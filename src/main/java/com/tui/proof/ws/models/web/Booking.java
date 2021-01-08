/**
 * 
 */
package com.tui.proof.ws.models.web;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 2-gen-2021
 * 
 * @Date : 2-gen-2021
 * 
 * @Time : 15.13.00
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.web.Booking
 * 
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {

	private String bookingId;

	@NotEmpty
	private BookingHolder holder;

	@NotEmpty
	private List<Flight> flights;
}
