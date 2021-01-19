package com.mobile.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private Filter filter;
	
	@Autowired
	private JWTAuthentication jwtAuthentication;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
		.authorizeRequests()
		.antMatchers("/authenticate", "/register")
		.permitAll()
		.anyRequest()
		.authenticated()
		.and().exceptionHandling().authenticationEntryPoint(jwtAuthentication)
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	}
}
