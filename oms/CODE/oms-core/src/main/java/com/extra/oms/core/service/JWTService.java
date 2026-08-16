package com.extra.oms.core.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.extra.oms.core.bean.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service("jwtService")
public class JWTService {

	private static final Logger _LOG = Logger.getLogger(JWTService.class);

	@Value("${jwt.secret}")
	private String jwtSecret;

	private ObjectMapper mapper = new ObjectMapper();

	public String generateAuthToken(UserInfo userInfo) {
		return Jwts.builder().setIssuedAt(new Date()).claim("payload", userInfo).signWith(SignatureAlgorithm.HS384, jwtSecret.getBytes()).compact();
	}

	public boolean verifyToken(String token, HttpServletRequest request) {
		try {			
			Object payload = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody().get("payload");
			request.setAttribute("user", mapper.convertValue(payload, UserInfo.class));
			return true;
		} catch (Exception e) {
			_LOG.error("Error while authentication", e);
			return false;
		}
	}
}
