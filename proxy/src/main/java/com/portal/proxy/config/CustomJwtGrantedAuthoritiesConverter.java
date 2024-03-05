package com.portal.proxy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	Logger LOG = LoggerFactory.getLogger(CustomJwtGrantedAuthoritiesConverter.class);

	String clientName;

	public CustomJwtGrantedAuthoritiesConverter(String clientName) {
		super();
		this.clientName = clientName;
	}

	@Override
	public <U> Converter<Jwt, U> andThen(Converter<? super Collection<GrantedAuthority>, ? extends U> after) {
		return Converter.super.andThen(after);
	}

	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {

		Collection<GrantedAuthority> finalRoles = new ArrayList<>();
		extractRealmRoles(jwt.getClaim("realm_access"), finalRoles);
	    extractClientRoles(jwt, finalRoles);
		LOG.info(finalRoles.toString());
	    return finalRoles;
	}

	private void extractRealmRoles(Object mapObject, Collection<GrantedAuthority> finalRoles) {
	    if (mapObject instanceof Map) {
	        Map<String, List<String>> rolesMap = (Map<String, List<String>>) mapObject;
	        rolesMap.computeIfAbsent("roles", key -> new ArrayList<>())
	                .forEach(role -> finalRoles.add(new SimpleGrantedAuthority(role)));
	    }
	}

	private void extractClientRoles(Jwt jwt, Collection<GrantedAuthority> finalRoles) {
	    Map<String, Map<String, List<String>>> resourceAccessMap = jwt.getClaim("resource_access");
	    if (resourceAccessMap != null) {
	        resourceAccessMap.computeIfAbsent(this.clientName, key -> Collections.emptyMap())
	                .computeIfAbsent("roles", key -> new ArrayList<>())
	                .forEach(role -> finalRoles.add(new SimpleGrantedAuthority(role)));
	    }
	}
}
