/**
 * 
 */
package com.extra.oms.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.extra.oms.common.BaseException;
import com.extra.oms.core.bean.UserInfo;

/**
 * @author aibrahim
 *
 */
@Repository
public class UserDAO extends BaseDAO {

	private static final String GET_USER_BY_NAME_QUERY = "SELECT U.USER_ID, U.USER_NAME, U.PASSWORD, R.ROLES FROM OMS_USER_INFO U, OMS_ROLE_INFO R WHERE U.ROLE_ID = R.ROLE_ID AND USER_NAME = :userName ";

	public UserInfo getUserByUserName(String userName) throws BaseException {

		return jdbcTemplate.queryForObject(GET_USER_BY_NAME_QUERY, Collections.singletonMap("userName", userName), new RowMapper<UserInfo>() {

			@Override
			public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserInfo userInfo = new UserInfo();
				userInfo.setId(rs.getLong(1));
				userInfo.setUserName(rs.getString(2));
				userInfo.setPassword(rs.getString(3));
				userInfo.setPrivileges(Arrays.asList(StringUtils.split(rs.getString(4), ","))); ;
				return userInfo;
			}
		});
	}
}
