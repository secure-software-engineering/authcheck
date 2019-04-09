package com.example.demo.security;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class UserAuthenticationManager implements AuthenticationManager {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication.getPrincipal() == null || authentication.getCredentials() == null) {
            throw new AuthenticationCredentialsNotFoundException("No credentials transmitted.");
        }

        try {
            Path absPath = Paths.get("src/main/resources/users.json");
            String jsonData = new String(Files.readAllBytes(absPath));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode obj = mapper.readTree(jsonData);

            for (JsonNode userJSON : obj) {
                String username = userJSON.get("username").asText();
                String password = userJSON.get("password").asText();
                if (authentication.getPrincipal().toString().equals(username)) {
                    if (authentication.getCredentials().toString().trim().equals(password)) {
                        JsonNode authoritiesJSON = userJSON.get("authorities");
                        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                        for (JsonNode authorityJSON : authoritiesJSON) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + authorityJSON.asText()));
                        }
                        return new UsernamePasswordAuthenticationToken(username, password, authorities);
                    } else {
                        throw new AuthenticationCredentialsNotFoundException("Password is wrong.");
                    }
                }
            }
            throw new AuthenticationCredentialsNotFoundException("Username not found.");
        } catch (IOException e) {
            System.out.println("Can not find json file.");
            return null;
        }
    }
}
