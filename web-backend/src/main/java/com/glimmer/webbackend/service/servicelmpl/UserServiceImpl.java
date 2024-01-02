package com.glimmer.webbackend.service.servicelmpl;

import com.glimmer.webbackend.entity.User;
import com.glimmer.webbackend.repository.UserDao;
import com.glimmer.webbackend.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassNAME UserServicelmpl
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/1 02:10
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService
{
  @Resource
  private UserDao userDao;

  @Override
  public User getUserById(Long id)
  {
    User user = userDao.getOne(id);
    return user;
  }

  @Override
  public User createUser(User user)
  {
    if(userDao.findByUsername(user.getUsername())!=null){
      return null;
    }else{
      User newUser = userDao.save(user);
      return newUser;
    }
  }

  @Override
  public User updateUser(User user)
  {
    userDao.flush();
    return user;
  }

  @Override
  public boolean existsByUsername(String username)
  {
    return userDao.existsByUsername(username);
  }

  @Override
  public boolean existsById(Long id)
  {
    return userDao.existsById(id);
  }
}
