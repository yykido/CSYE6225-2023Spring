package com.glimmer.webbackend.service;

import com.glimmer.webbackend.entity.User;

/**
 * @ClassNAME UserService
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/1 02:09
 * @Version 1.0
 */
public interface UserService
{
  User getUserById(Long id);
  User createUser(User user);
  User updateUser(User user);

  boolean existsByUsername(String username);
  boolean existsById(Long id);
}
