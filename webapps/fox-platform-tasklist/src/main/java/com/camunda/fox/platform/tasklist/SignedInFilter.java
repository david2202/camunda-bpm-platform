package com.camunda.fox.platform.tasklist;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "SignedInFilter", urlPatterns = { "/app/*" })
public class SignedInFilter implements Filter {

  private final static Logger log = Logger.getLogger(SignedInFilter.class.getSimpleName());
  
  @Inject
  Identity identity;

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    
    log.info("Checking if user is signed in");
    
    if (!identity.isSignedIn()) {
      log.info("Redirecting to sign in page");
      ((HttpServletResponse) servletResponse).sendRedirect(((HttpServletRequest) servletRequest).getContextPath() + "/signin.jsf");
    } else {
      log.info("User is signed in, continuing filter chain");
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

}