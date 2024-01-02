package com.glimmer.webbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassNAME ProductImageResponse
 * @Description TODO
 * @Author glimmer
 * @Date 2023/3/2 16:25
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@ResponseBody
public class ProductImageResponse
{
  private Long image_id;
  private Long product_id;
  private String file_name;
  private String date_created;
  private String s3_bucket_path;
}
