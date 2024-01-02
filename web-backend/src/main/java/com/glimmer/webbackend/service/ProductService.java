package com.glimmer.webbackend.service;

import com.glimmer.webbackend.entity.Product;

/**
 * @ClassNAME ProductService
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/8 14:53
 * @Version 1.0
 */
public interface ProductService
{

  Product getProductById(Long id);
  Product createProduct(Product product);
  void updateProduct();
  void deleteProductById(Long id);
  boolean existsBySku(String sku);
  boolean existsById(Long id);
}
