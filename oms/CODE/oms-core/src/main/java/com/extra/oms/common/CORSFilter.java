/**
 * 
 */
package com.extra.oms.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author aibrahim
 *
 */
public class CORSFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		res.addHeader("Access-Control-Allow-Origin", "*");
		if (req.getHeader("Access-Control-Request-Method") != null || req.getHeader("Access-Control-Request-Headers") != null) {
			res.addHeader("Access-Control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
			res.addHeader("Access-Control-Allow-Methods", req.getHeader("Access-Control-Request-Method"));
			res.setStatus(HttpServletResponse.SC_CREATED);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		
	}
}
