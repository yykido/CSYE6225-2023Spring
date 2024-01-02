package com.glimmer.webbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassNAME UpdateUserRequest
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/8 00:04
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class UpdateUserRequest
{
  private String first_name;
  private String last_name;
  private String password;
  public boolean isEmpty()
  {
    return first_name == null && last_name == null && password == null;
  }

  public boolean hasEmpty()
  {
    return first_name == null || last_name == null || password == null;
  }

}
