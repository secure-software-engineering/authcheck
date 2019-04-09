package com.example.demo.service;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import com.example.demo.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Service for operations on users.
 */
public class UserService {

  /**
   * Returns a user object that represents the currently logged in user.
   * @return Logged in user.
   */
  public User getUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return new User((String) auth.getPrincipal(), this.convertAuthorities(auth.getAuthorities()));
  }

  /**
   * Converts the authorities to a list of strings.
   *
   * @param authorityCollection Assigned authorities for the logged in user.
   * @return List of authority strings.
   */
  private List<String> convertAuthorities(
      Collection<? extends GrantedAuthority> authorityCollection) {
    List<String> authorities = new ArrayList<String>();
    for (GrantedAuthority authority : authorityCollection) {
      authorities.add(authority.getAuthority());
    }
    return authorities;
  }
}