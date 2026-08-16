/**
 * 
 */
package com.extra.oms.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author aibrahim
 *
 */
public abstract class BaseDAO {

	@Autowired
	@Qualifier("jdbcTemplate")
	protected NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("simJdbcTemplate")
	protected NamedParameterJdbcTemplate simJdbcTemplate;
}
