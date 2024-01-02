package com.glimmer.webbackend.entity;

/**
 * @ClassNAME User
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/1 02:09
 * @Version 1.0
 */
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Table(name = "user")
@Entity
@Data
@NoArgsConstructor
public class User
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Size(min=1, max=255)
  @NotEmpty
  private String username;
  @Size(min=1, max=255)
  @NotEmpty
  private String password;
  @Size(min=1, max=255)
  @NotEmpty
  private String first_name;
  @Size(min=1, max=255)
  @NotEmpty
  private String last_name;
  private String account_created;
  private String account_updated;

//  public boolean isUnValid()
//  {
//    return username == null ||
//           password == null ||
//           first_name == null ||
//           last_name == null ||
//           password.length() == 0 ||
//           password.length() > 255 ||
//           first_name.length() == 0 ||
//           first_name.length() > 255 ||
//           last_name.length() == 0 ||
//           last_name.length() > 255;
//  }
}

