package com.example.demo.security;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/version").permitAll()
        .antMatchers(HttpMethod.GET, "/todo/{id}").access("hasRole('USER') or hasRole('ADMIN')")
        .antMatchers(HttpMethod.GET, "/todo").access("hasAnyRole('USER', 'ADMIN')")
        .antMatchers(HttpMethod.POST, "/todo").authenticated()
        .antMatchers(HttpMethod.DELETE, "/todo/{id}").access("hasRole('ADMIN')")
        .antMatchers(HttpMethod.PATCH, "/todo/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/profile").permitAll()
        .and()
        .httpBasic();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.parentAuthenticationManager(new UserAuthenticationManager());
  }
}
