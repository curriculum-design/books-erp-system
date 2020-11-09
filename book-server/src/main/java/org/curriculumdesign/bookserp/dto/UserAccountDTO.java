package org.curriculumdesign.bookserp.dto;


import com.vanrui.spring.boot.starter.security.base.UserAccount;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
public class UserAccountDTO implements UserDetails, UserAccount {
    private Long id;
    private String username;
    private Integer status;
    private String password;
    private String credentialsSalt;
    private Set<String> permissions;
    private Set<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status == 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status == 0;
    }

    @Override
    public boolean isEnabled() {
        return status == 0;
    }
}
