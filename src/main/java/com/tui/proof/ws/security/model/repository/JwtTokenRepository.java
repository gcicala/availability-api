/**
 * 
 */
package com.tui.proof.ws.security.model.repository;

import java.util.List;

import com.tui.proof.ws.security.exception.JwtException;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 13.07.20
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.JwtTokenRepository
 * 
 */
public interface JwtTokenRepository<T> {

	public Class<T> getRepositoryClass();

	public T findByUsername(
			String username) throws JwtException;

	public List<T> findAll() throws JwtException;

	public T save(
			T entity) throws JwtException;
}
