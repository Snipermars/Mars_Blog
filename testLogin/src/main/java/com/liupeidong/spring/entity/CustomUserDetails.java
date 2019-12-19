package com.liupeidong.spring.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * @creator liupeidong
 * @createTime 2019-11-27 17:23
 * @description
 **/
public class CustomUserDetails implements UserDetails, Serializable {
    private static final long serialVersionUID = 6770898962346319228L;

    final String password;

    final String username;//用户名
    final User user;
    final boolean enabled;
    final boolean accountNonExpired;
    final boolean credentialsNonExpired;
    final boolean accountNonLocked;
    final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String password, String username, User user, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        this.password = password;
        this.username = username;
        this.user = user;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public User getUser() {
        return user;
    }
}
