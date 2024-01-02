package com.glimmer.webbackend.repository;

import com.glimmer.webbackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassNAME ProductDao
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/8 14:59
 * @Version 1.0
 */
public interface ProductDao extends JpaRepository<Product, Long>
{
  Product getOne(Long id);
  boolean existsBySku(String sku);

}
