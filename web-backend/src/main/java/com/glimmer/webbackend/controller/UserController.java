package com.glimmer.webbackend.controller;

import com.glimmer.webbackend.dto.request.UpdateUserRequest;
import com.glimmer.webbackend.dto.response.UserResponse;
import com.glimmer.webbackend.entity.User;
import com.glimmer.webbackend.dto.ResponseCode;
import com.glimmer.webbackend.repository.repositoryImpl.MyUserDetails;
import com.glimmer.webbackend.service.UserService;
import com.glimmer.webbackend.utils.TimeTool;
import com.glimmer.webbackend.utils.ValidateTool;
import com.timgroup.statsd.StatsDClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static com.glimmer.webbackend.utils.DataTool.checkEmailFormat;

/**
 * @ClassNAME UserController
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/1 02:07
 * @Version 1.0
 */
@RestController
@RequestMapping("/v2")
@Slf4j
public class UserController
{
  @Autowired
  private UserService userService;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private StatsDClient statsDClient;

  @PostMapping("/user")
  public UserResponse registerUser(@RequestBody User newUser, HttpServletResponse response){
    statsDClient.incrementCounter("endpoint.user.post");
    String username = newUser.getUsername();
    if(!ValidateTool.isValid(newUser) ||
       !checkEmailFormat(username) ||
       userService.existsByUsername(username)){
       response.setStatus(ResponseCode.ERROR);
       log.error("user not valid or exists");
      return null;
    }

    String nowTime = TimeTool.getNowTime();

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(newUser.getPassword()));
    user.setFirst_name(newUser.getFirst_name());
    user.setLast_name(newUser.getLast_name());
    user.setAccount_created(nowTime);
    user.setAccount_updated(nowTime);
    User savedUser = userService.createUser(user);
    log.info("create a user (id: {})", savedUser.getId() );
    response.setStatus(ResponseCode.SUCCESS_CREATE);
    return filterUserData(savedUser);
  }



  @GetMapping("/user/{id}")
  public UserResponse getUser(@PathVariable("id") Long id, @AuthenticationPrincipal MyUserDetails currentUser, HttpServletResponse response){
    statsDClient.incrementCounter("endpoint.user.id.get");
    long currentUserId = currentUser.getId();
    if(id != currentUserId){
      response.setStatus(ResponseCode.ERROR_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return null;
    }

    User user = userService.getUserById(id);

    if(user!=null){
      response.setStatus(ResponseCode.SUCCESS);
      log.info("get a user (id: {})", user.getId() );
      return filterUserData(user);
    }else{
      response.setStatus(ResponseCode.ERROR_NOT_FOUND);
      log.error("user (id: {}) not exist", user.getId() );
      return null;
    }
  }

  @PutMapping("/user/{id}")
  public void updateUser(@PathVariable("id") long id, @RequestBody UpdateUserRequest newUser, @AuthenticationPrincipal MyUserDetails currentUser, HttpServletResponse response)
  {
    statsDClient.incrementCounter("endpoint.user.id.put");
    long currentUserId = currentUser.getId();
    if(id != currentUserId){
      response.setStatus(ResponseCode.ERROR_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return ;
    }

    if(newUser.hasEmpty()){
      response.setStatus(ResponseCode.ERROR);
      log.error("user has empty fields");
    }
    else{
      User user = userService.getUserById(id);
      if (user != null) {
        if(newUser.getFirst_name() != null){
          user.setFirst_name(newUser.getFirst_name());
        }
        if(newUser.getLast_name() != null){
          user.setLast_name(newUser.getLast_name());
        }
        if(newUser.getPassword() != null){
          user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        user.setAccount_updated(TimeTool.getNowTime());

        if(!ValidateTool.isValid(user)){
          response.setStatus(ResponseCode.ERROR);
          log.error("user not valid");
        }
        else{
          userService.updateUser(user);
          log.info("update a user (id: {})", user.getId() );
          response.setStatus(ResponseCode.SUCCESS_UPDATE);
        }
      } else {
        response.setStatus(ResponseCode.ERROR_NOT_FOUND);
        log.error("user (id: {}) not exist", user.getId() );
      }
    }
  }

  private UserResponse filterUserData(User user)
  {
    UserResponse publicUserInformation = new UserResponse(
        user.getId(),
        user.getUsername(),
        user.getFirst_name(),
        user.getLast_name(),
        user.getAccount_created(),
        user.getAccount_updated());
    return publicUserInformation;
  }

}
