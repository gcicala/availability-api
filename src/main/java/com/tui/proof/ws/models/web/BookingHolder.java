/**
 * 
 */
package com.tui.proof.ws.models.web;

import java.util.List;

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
 * @Time : 14.59.13
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.web.BokingHolder
 * 
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingHolder {

	private String name;
	private String lastName;
	private String address;
	private String postalCode;
	private String country;
	private String email;
	private List<String> telephones;
}
