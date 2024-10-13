package org.ms.authentificationservice.filtres;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final String token;
    private final Set<GrantedAuthority> authorities;

    public JwtAuthenticationToken(Object principal, String token, Set<GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
        this.authorities = authorities;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}

