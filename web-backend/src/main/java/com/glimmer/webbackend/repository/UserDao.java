package com.glimmer.webbackend.repository;

import com.glimmer.webbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassNAME UserDao
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/1 02:09
 * @Version 1.0
 */
public interface UserDao extends JpaRepository<User, Long>
{
  User getOne(Long id);
  User findByUsername(String username);
  boolean existsByUsername(String username);
  boolean existsById(Long id);
}
