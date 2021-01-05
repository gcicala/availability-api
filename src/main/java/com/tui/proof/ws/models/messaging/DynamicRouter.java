/**
 * 
 */
package com.tui.proof.ws.models.messaging;

import com.tui.proof.ws.exception.ServiceException;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 29-dic-2020
 * 
 * @Date : 29-dic-2020
 * 
 * @Time : 18.20.57
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.models.messaging.DynamicRouter
 * 
 */
public interface DynamicRouter<E extends Message> {

	public abstract void dispatch(
			E content) throws ServiceException;
}