package com.sellingPartners.backEnd.entity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN(Set.of(
            Permission.READ,
            Permission.CREATE,
            Permission.UPDATE,
            Permission.DELETE
    )),
    PARTNER(Set.of(
    		Permission.READ,
    		Permission.CREATE,
    		Permission.UPDATE,
    		Permission.DELETE
    )),
    COMPANY(Set.of(
    		Permission.READ,
    		Permission.CREATE,
    		Permission.UPDATE,
    		Permission.DELETE
    ));

    private final Set<Permission> permissions;  //Permission enum class

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}