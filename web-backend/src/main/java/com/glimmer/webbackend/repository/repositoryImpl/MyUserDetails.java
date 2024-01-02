package com.glimmer.webbackend.repository.repositoryImpl;

import java.util.Arrays;
import java.util.Collection;

import com.glimmer.webbackend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @ClassNAME MyUserDetails
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/1 22:32
 * @Version 1.0
 */
public class MyUserDetails implements UserDetails
{
  private User user;

  public MyUserDetails(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("User");
    return Arrays.asList(authority);
  }

  public Long getId() {
    return user.getId();
  }
  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
