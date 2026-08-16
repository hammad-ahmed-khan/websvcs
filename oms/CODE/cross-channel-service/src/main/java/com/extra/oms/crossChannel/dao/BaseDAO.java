package com.extra.oms.crossChannel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author aibrahim
 *
 */
public abstract class BaseDAO {

	@Autowired
	protected NamedParameterJdbcTemplate jdbcTemplate;
}
