/**
 * 
 */
package com.extra.oms.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.oms.common.BaseException;
import com.extra.oms.common.security.PooledStringDigester;
import com.extra.oms.core.bean.UserInfo;
import com.extra.oms.core.dao.UserDAO;

/**
 * @author aibrahim
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private PooledStringDigester pooledDigester;

	public UserInfo validateUser(UserInfo userInfo) throws BaseException {
		UserInfo user = userDAO.getUserByUserName(userInfo.getUserName());
		if (user == null) {
			throw new BaseException("INVALID_USER_NAME_PASSWORD");
		}
		if (!pooledDigester.matches(userInfo.getPassword(), user.getPassword())) {
			throw new BaseException("INVALID_USER_NAME_PASSWORD");
		}
		user.setPassword(null);
		return user;
	}
}
