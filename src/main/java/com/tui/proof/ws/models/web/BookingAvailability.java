/**
 * 
 */
package com.tui.proof.ws.models.web;

import java.time.LocalDate;
import java.util.Currency;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 29-dic-2020
 * 
 * @Date : 29-dic-2020
 * 
 * @Time : 16.37.07
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.web.BookingAvailability
 * 
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingAvailability implements BookingModel {

	@NotBlank(message = "The origin Airport is mandatory")
	@Size(min = 4, max = 15, message = "The Airport accept min 4 max 15 characters")
	private String originAirport;

	@NotBlank(message = "The destination Airport is mandatory")
	@Size(min = 4, max = 15, message = "The Airport accept min 4 max 15 characters")
	private String destinationAirport;

	@NotNull(message = "The Currency is mandatory")
	private Currency currency;

	@NotNull(message = "The startDate is mandatory")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate startDate;

	@NotNull(message = "The endDate is mandatory")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate endDate;

	@NotNull(message = "The Paxe is mandatory")
	private Paxe paxes;

}
