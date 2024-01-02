package com.glimmer.webbackend.service.servicelmpl;

import com.glimmer.webbackend.entity.User;
import com.glimmer.webbackend.repository.UserRepository;
import com.glimmer.webbackend.repository.repositoryImpl.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @ClassNAME UserDetailsServiceImpl
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/1 22:37
 * @Version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    User user = userRepository.getUserByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("Could not find user");
    }

    return new MyUserDetails(user);
  }
}
