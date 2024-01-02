package com.glimmer.webbackend.repository;

import com.glimmer.webbackend.entity.Product;
import com.glimmer.webbackend.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ClassNAME ProductImageDap
 * @Description TODO
 * @Author glimmer
 * @Date 2023/3/2 13:32
 * @Version 1.0
 */
public interface ProductImageDao extends JpaRepository<ProductImage, Long>
{
  ProductImage getOne(Long id);
  boolean existsById(Long id);
  List<ProductImage> getProductImagesByProductId(Long productId);
}
