/**
 * 
 */
package com.extra.oms.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.common.BaseException;
import com.extra.oms.core.bean.UserInfo;

/**
 * @author aibrahim
 *
 */
@RestController()
@RequestMapping(path = "/user")
public class UserController {

	@GetMapping
	public UserInfo getUserInfo(@RequestAttribute(name = "user") UserInfo user) throws BaseException {
		return user;
	}
}
