package com.prometheus.core.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.prometheus.core.configuration.ConfigurationContext;
import com.prometheus.core.exception.ConfigurationException;

/**
 * 
 * @author alt
 *
 */
public class SecurityFilter implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {

		try {
			ConfigurationContext.loadConfiguration();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		chain.doFilter(request, response);
	}

}