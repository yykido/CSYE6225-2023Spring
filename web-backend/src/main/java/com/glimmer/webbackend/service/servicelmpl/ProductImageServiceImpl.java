package com.glimmer.webbackend.service.servicelmpl;

import com.glimmer.webbackend.entity.ProductImage;
import com.glimmer.webbackend.repository.ProductImageDao;
import com.glimmer.webbackend.service.ProductImageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassNAME ProductImageServiceImpl
 * @Description TODO
 * @Author glimmer
 * @Date 2023/3/2 13:28
 * @Version 1.0
 */
@Service
public class ProductImageServiceImpl implements ProductImageService
{
  @Resource
  private ProductImageDao productImageDao;
  @Override
  public ProductImage createProductImage(ProductImage productImage)
  {
    ProductImage savedProductImage = productImageDao.save(productImage);
    return savedProductImage;
  }

  @Override
  public ProductImage getProductImageById(Long id)
  {
    ProductImage productImage = productImageDao.getOne(id);
    return productImage;
  }

  @Override
  public void updateProductImage()
  {
    productImageDao.flush();
  }

  @Override
  public void deleteProductImageById(Long id)
  {
    productImageDao.deleteById(id);
  }

  @Override
  public boolean existsById(Long id)
  {
    return productImageDao.existsById(id);
  }

  @Override
  public List<ProductImage> getProductImagesByProductId(Long productId)
  {
    List<ProductImage> productImages = productImageDao.getProductImagesByProductId(productId);
    return productImages;
  }
}
