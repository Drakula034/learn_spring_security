package com.eazybytes.filter;

import jakarta.servlet.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.logging.Logger;

public class AuthoritiesLoggingAtFilter implements Filter {

    private final Logger LOG = Logger.getLogger(AuthoritiesLoggingAfterFilter.class.getName());
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        LOG.info("Authentication validation is in progress");

        chain.doFilter(request, response);
    }
}
