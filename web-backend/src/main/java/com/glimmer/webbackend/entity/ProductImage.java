package com.glimmer.webbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassNAME ProductImage
 * @Description TODO
 * @Author glimmer
 * @Date 2023/3/2 12:46
 * @Version 1.0
 */
@Table(name = "productImage")
@Entity
@Data
@NoArgsConstructor
public class ProductImage
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long productId;
  private String fileName;
  private String dateCreated;
  private String s3BucketPath;
}
