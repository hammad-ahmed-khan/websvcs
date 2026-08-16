package com.extra.oms.core.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.extra.oms.common.BaseException;
import com.extra.oms.core.bean.UserInfo;
import com.extra.oms.core.service.JWTService;
import com.extra.oms.core.service.UserService;

@RestController
public class LoginController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private JWTService jwtService;

	@PostMapping(path = "/login")
	public void validateUser(@RequestBody UserInfo userInfo, HttpServletResponse response) throws BaseException {
		UserInfo user = userService.validateUser(userInfo);
		String token = jwtService.generateAuthToken(user);
		response.setHeader("X-AUTH-TOKEN", token);
		response.setHeader("Access-Control-Expose-Headers", "X-AUTH-TOKEN");
	}
}
