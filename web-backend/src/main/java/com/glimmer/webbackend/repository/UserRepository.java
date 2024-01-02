package com.glimmer.webbackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.glimmer.webbackend.entity.User;
/**
 * @ClassNAME UserRepository
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/1 22:29
 * @Version 1.0
 */
public interface UserRepository extends CrudRepository<User, Long> {

  @Query("SELECT u FROM User u WHERE u.username = :username")
  public User getUserByUsername(@Param("username") String username);
}
