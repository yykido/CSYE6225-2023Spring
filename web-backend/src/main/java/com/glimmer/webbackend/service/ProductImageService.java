package com.glimmer.webbackend.service;

import com.glimmer.webbackend.entity.ProductImage;
import java.util.List;

/**
 * @ClassNAME ProductImageService
 * @Description TODO
 * @Author glimmer
 * @Date 2023/3/2 13:26
 * @Version 1.0
 */
public interface ProductImageService
{
  ProductImage createProductImage(ProductImage productImage);
  ProductImage getProductImageById(Long id);
  void updateProductImage();
  void deleteProductImageById(Long id);
  boolean existsById(Long id);

  List<ProductImage> getProductImagesByProductId(Long productId);
}
