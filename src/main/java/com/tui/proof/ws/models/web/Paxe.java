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
 * @Copyright : 29-dic-2020
 * 
 * @Date : 29-dic-2020
 * 
 * @Time : 16.42.37
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.web.Paxe
 * 
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Paxe {

	private int infants;

	private int children;

	private int adults;

}
