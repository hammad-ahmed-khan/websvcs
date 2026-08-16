package com.extra.oms.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

import com.extra.oms.core.service.JWTService;

@WebFilter("/api/*")
public class AuthFilter implements Filter {

	private static final List<String> NON_AUTH_URLS;

	private static JWTService jwtService = null;

	static {
		NON_AUTH_URLS = new ArrayList<String>(1);
		NON_AUTH_URLS.add("/oms/api/login");
		NON_AUTH_URLS.add("/oms/api/order/validate");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String path = req.getRequestURI();
		String token;
		if (NON_AUTH_URLS.contains(path)) {
			chain.doFilter(request, response);
		} else if ((token = req.getHeader("X-AUTH-TOKEN")) != null && jwtService.verifyToken(token, req)) {
			chain.doFilter(request, response);
		} else {
			res.sendError(HttpStatus.UNAUTHORIZED.value());
		}
	}

	@Override
	public void destroy() {
	}

	public static void setJWTService(ApplicationContext applicationContext) {
		jwtService = applicationContext.getBean(JWTService.class);
	}
}
