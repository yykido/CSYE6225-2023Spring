package com.glimmer.webbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassNAME UserResponse
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/8 00:05
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@ResponseBody
public class UserResponse
{
  private Long id;
  private String username;
  private String first_name;
  private String last_name;
  private String account_created;
  private String account_updated;
}
