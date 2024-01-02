package com.glimmer.webbackend.service.servicelmpl;

import com.glimmer.webbackend.entity.Product;
import com.glimmer.webbackend.repository.ProductDao;
import com.glimmer.webbackend.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassNAME ProductServiceImpl
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/8 14:57
 * @Version 1.0
 */
@Service
public class ProductServiceImpl implements ProductService
{

  @Resource
  private ProductDao productDao;
  @Override
  public Product getProductById(Long id)
  {
    Product product = productDao.getOne(id);
    return product;
  }

  @Override
  public Product createProduct(Product product)
  {
    if(productDao.existsBySku(product.getSku())){
      throw new RuntimeException("SKU is exist");
    }
    else {
      Product savedProduct = productDao.save(product);
      return savedProduct;
    }
  }

  @Override
  public void updateProduct()
  {
    productDao.flush();
  }

  @Override
  public void deleteProductById(Long id)
  {
    Product product = productDao.getOne(id);
    if (product != null) {
      productDao.delete(product);
    }
    else {
      throw new RuntimeException("Product is not exist");
    }
  }

  @Override
  public boolean existsBySku(String sku)
  {
    return productDao.existsBySku(sku);
  }

  @Override
  public boolean existsById(Long id)
  {
    return productDao.existsById(id);
  }
}
