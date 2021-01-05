/**
 * 
 */
package com.tui.proof.ws.models.web;

import java.util.List;

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
 * @Time : 15.18.22
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.web.AvailabilityResponse
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailabilityResponse extends Response<List<Flight>> {

}
