package com.glimmer.webbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @ClassNAME Product
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/8 14:20
 * @Version 1.0
 */
@Table(name = "product")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Size(min=1, max=255)
  @NotEmpty
  private String name;
  @Size(min=1, max=255)
  @NotEmpty
  private String description;
  @Size(min=1, max=255)
  @NotEmpty
  @Column(unique = true)
  private String sku;
  @Size(min=1, max=255)
  @NotEmpty
  private String manufacturer;
  @NonNull
  @Max(100)
  @Min(0)
  private Long quantity;
  private String date_added;
  private String date_last_updated;
  private Long owner_user_id;

//  public boolean isUnValid()
//  {
//    return name == null ||
//           description == null ||
//           sku == null ||
//           manufacturer == null ||
//           quantity == null ||
//           name.length() == 0 ||
//           name.length() > 255 ||
//           description.length() == 0 ||
//           description.length() > 255 ||
//           sku.length() == 0 ||
//           sku.length() > 255 ||
//           manufacturer.length() == 0 ||
//           manufacturer.length() > 255 ||
//           quantity < 0 ||
//           quantity > 100;
//  }
}
