/**
 * 
 */
package com.extra.oms.core.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author aibrahim
 *
 */
public abstract class BaseDAO {

	@Autowired
	@Qualifier("jndiTemplate")
	protected NamedParameterJdbcTemplate jdbcTemplate;

	protected void appendWhere(StringBuilder queryBuilder) {
		if (queryBuilder.indexOf("WHERE") > -1) {
			queryBuilder.append(" AND ");
		} else {
			queryBuilder.append(" WHERE ");
		}
	}
}
