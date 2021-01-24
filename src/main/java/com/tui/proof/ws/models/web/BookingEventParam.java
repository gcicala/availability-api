/**
 * 
 */
package com.tui.proof.ws.models.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 20-gen-2021
 * 
 * @Date : 20-gen-2021
 * 
 * @Time : 20.10.45
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.web.BookingEventParam
 * 
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingEventParam implements BookingModel {

	public String name;

	public String value;
}
