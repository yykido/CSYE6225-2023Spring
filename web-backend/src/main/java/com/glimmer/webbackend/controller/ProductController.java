package com.glimmer.webbackend.controller;

import com.glimmer.webbackend.dto.response.ProductImageResponse;
import com.glimmer.webbackend.entity.Product;
import com.glimmer.webbackend.entity.ProductImage;
import com.glimmer.webbackend.repository.repositoryImpl.MyUserDetails;
import com.glimmer.webbackend.service.AmazonBucketService;
import com.glimmer.webbackend.service.ProductImageService;
import com.glimmer.webbackend.service.ProductService;
import com.glimmer.webbackend.service.UserService;
import com.glimmer.webbackend.utils.ValidateTool;
import com.timgroup.statsd.StatsDClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import com.glimmer.webbackend.utils.TimeTool;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
/**
 * @ClassNAME ProductController
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/8 15:09
 * @Version 1.0
 */
@RestController
@RequestMapping("/v2")
@Slf4j
public class ProductController
{
  @Autowired
  private ProductService productService;

  @Autowired
  private UserService userService;

  @Autowired
  private AmazonBucketService amazonBucketService;

  @Autowired
  private ProductImageService productImageService;

  @Autowired
  private StatsDClient statsDClient;

  @Value("${bucket_name}")
  private String bucketName;


  @GetMapping("/product/{productId}")
  public Product getProductById(@PathVariable Long productId, HttpServletResponse response)
  {
    statsDClient.incrementCounter("endpoint.product.id.get");
    if(productService.existsById(productId)){
      Product product = productService.getProductById(productId);
      log.info("get product (id: {})", product.getId() );
      return product;
    }
    else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      log.error("product (id: {}) not exists", productId);
      return null;
    }
  }

  @PostMapping("/product")
  public Product createProduct(@RequestBody Product product,
                               @AuthenticationPrincipal MyUserDetails currentUser,
                               HttpServletResponse response)
  {
    statsDClient.incrementCounter("endpoint.product.post");
    Long currentUserId = currentUser.getId();
    if(userService.existsById(currentUserId)){
      product.setOwner_user_id(currentUserId);
      String nowTime = TimeTool.getNowTime();
      product.setDate_added(nowTime);
      product.setDate_last_updated(nowTime);
      if(!ValidateTool.isValid(product) ||
         productService.existsBySku(product.getSku())){
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("product not valid or exists");
        return null;
      }
      else {
        Product newProduct = productService.createProduct(product);
        response.setStatus(HttpServletResponse.SC_CREATED);
        log.info("create product (id: {})", newProduct.getId() );
        return newProduct;
      }
    }
    else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return null;
    }
  }

  @PutMapping("/product/{productId}")
  public void updateProduct(@PathVariable Long productId,
                               @RequestBody Product product,
                               @AuthenticationPrincipal MyUserDetails currentUser,
                               HttpServletResponse response)
  {
    statsDClient.incrementCounter("endpoint.product.id.put");
    Long currentUserId = currentUser.getId();
    if(!userService.existsById(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return ;
    }

    if( !ValidateTool.isValid(product) || ValidateTool.hasWrongFields(product)){
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      log.error("product not valid");
      return ;
    }

    if(!productService.existsById(productId)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      log.error("product (id: {}) not exists", productId);
      return ;
    }

    Product oldProduct = productService.getProductById(productId);
    if(!oldProduct.getOwner_user_id().equals(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return ;
    }
    oldProduct.setName(product.getName());
    oldProduct.setDescription(product.getDescription());
    if(product.getSku() != null && !oldProduct.getSku().equals(product.getSku())){
      if(productService.existsBySku(product.getSku())){
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("product (sku: {}) exists", product.getSku());
        return ;
      }
      oldProduct.setSku(product.getSku());
    }
    oldProduct.setManufacturer(product.getManufacturer());
    oldProduct.setQuantity(product.getQuantity());
    oldProduct.setDate_last_updated(TimeTool.getNowTime());
    productService.updateProduct();
    log.info("update product (id: {})", oldProduct.getId() );
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  @PatchMapping("/product/{productId}")
  public void patchProduct(@PathVariable Long productId,
                              @RequestBody Product product,
                              @AuthenticationPrincipal MyUserDetails currentUser,
                              HttpServletResponse response)
  {
    statsDClient.incrementCounter("endpoint.product.id.patch");
    Long currentUserId = currentUser.getId();
    if(!userService.existsById(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return ;
    }

    if(!productService.existsById(productId)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      log.error("product (id: {}) not exists", productId);
      return ;
    }

    if(ValidateTool.isEmpty(product)){
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      log.error("product is empty");
      return ;
    }

    Product oldProduct = productService.getProductById(productId);
    if(!oldProduct.getOwner_user_id().equals(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return ;
    }
    if(product.getName() != null){
      oldProduct.setName(product.getName());
    }
    if(product.getDescription() != null){
      oldProduct.setDescription(product.getDescription());
    }
    if(product.getSku() != null && !oldProduct.getSku().equals(product.getSku())){
      if(productService.existsBySku(product.getSku())){
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("product (sku: {}) exists", product.getSku());
        return ;
      }
      oldProduct.setSku(product.getSku());
    }
    if(product.getManufacturer() != null){
      oldProduct.setManufacturer(product.getManufacturer());
    }
    if(product.getQuantity() != null){
      oldProduct.setQuantity(product.getQuantity());
    }

    oldProduct.setDate_last_updated(TimeTool.getNowTime());
    productService.updateProduct();
    log.info("patch product (id: {})", oldProduct.getId() );
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  @DeleteMapping("/product/{productId}")
  public void deleteProduct(@PathVariable Long productId,
                            @AuthenticationPrincipal MyUserDetails currentUser,
                            HttpServletResponse response)
  {
    statsDClient.incrementCounter("endpoint.product.id.delete");
    Long currentUserId = currentUser.getId();
    if(!userService.existsById(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return ;
    }

    if(!productService.existsById(productId)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      log.error("product (id: {}) not exists", productId);
      return ;
    }

    Product oldProduct = productService.getProductById(productId);
    if(!oldProduct.getOwner_user_id().equals(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return ;
    }

    productService.deleteProductById(productId);
    log.info("delete product (id: {})", oldProduct.getId() );
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  @PostMapping("/product/{productId}/image")
  public ProductImageResponse uploadImage(@PathVariable Long productId,
                                          @RequestParam("file") MultipartFile multipart,
                                          @AuthenticationPrincipal MyUserDetails currentUser,
                                          HttpServletResponse response)
  {
    statsDClient.incrementCounter("endpoint.product.id.image.post");
    Long currentUserId = currentUser.getId();
    if(!userService.existsById(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return null;
    }

    if(!productService.existsById(productId)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      log.error("product (id: {}) not exists", productId);
      return null;
    }

    Product oldProduct = productService.getProductById(productId);
    if(!oldProduct.getOwner_user_id().equals(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return null;
    }

    if(multipart.isEmpty()){
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      log.error("upload image is empty");
      return null;
    }

    String s3path = "product/image/" + UUID.randomUUID().toString();
    try {
      InputStream inputStream = multipart.getInputStream();
      String contentType = multipart.getContentType();
      String fileName = multipart.getOriginalFilename();
      amazonBucketService.upload(bucketName, s3path, inputStream, contentType);
      ProductImage productImage = new ProductImage();
      productImage.setProductId(productId);
      productImage.setS3BucketPath(s3path);
      productImage.setFileName(fileName);
      productImage.setDateCreated(TimeTool.getNowTime());
      productImageService.createProductImage(productImage);
      log.info("create product image (id: {})", productImage.getId());
      ProductImageResponse productImageResponse = new ProductImageResponse(
              productImage.getId(),
              productImage.getProductId(),
              productImage.getS3BucketPath(),
              productImage.getFileName(),
              productImage.getDateCreated()
      );
      return productImageResponse;
    }
    catch (Exception e){
      System.out.println(e.getMessage());
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      log.error("upload image failed, error => {}", e.getMessage());
    }
    return null;
  }

  @GetMapping("/product/{productId}/image")
  public List<ProductImageResponse> getProductImages(@PathVariable Long productId,
                                             @AuthenticationPrincipal MyUserDetails currentUser,
                                             HttpServletResponse response)
  {
    statsDClient.incrementCounter("endpoint.product.id.image.get");
    Long currentUserId = currentUser.getId();
    if(!userService.existsById(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return null;
    }

    if(!productService.existsById(productId)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      log.error("product (id: {}) not exists", productId);
      return null;
    }

    Product oldProduct = productService.getProductById(productId);
    if(!oldProduct.getOwner_user_id().equals(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return null;
    }

    List<ProductImage> productImages = productImageService.getProductImagesByProductId(productId);

    List<ProductImageResponse> productImageResponses = new ArrayList<>();
    for(ProductImage productImage : productImages){
      ProductImageResponse productImageResponse = new ProductImageResponse(
              productImage.getId(),
              productImage.getProductId(),
              productImage.getS3BucketPath(),
              productImage.getFileName(),
              productImage.getDateCreated()
      );
      productImageResponses.add(productImageResponse);
      log.info("get product images for product (id: {})", oldProduct.getId());
    }
    return productImageResponses;
  }


  @GetMapping("/product/{productId}/image/{imageId}")
  public ProductImageResponse getProductImage(@PathVariable Long productId,
                                      @PathVariable Long imageId,
                                      @AuthenticationPrincipal MyUserDetails currentUser,
                                      HttpServletResponse response)
  {
    statsDClient.incrementCounter("endpoint.product.id.image.id.get");
    Long currentUserId = currentUser.getId();
    if(!userService.existsById(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return null;
    }

    if(!productService.existsById(productId)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      log.error("product (id: {}) not exists", productId);
      return null;
    }

    Product oldProduct = productService.getProductById(productId);
    if(!oldProduct.getOwner_user_id().equals(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return null;
    }

    if(!productImageService.existsById(imageId)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      log.error("product image (id: {}) not exists", imageId);
      return null;
    }

    ProductImage productImage = productImageService.getProductImageById(imageId);
    if(!productImage.getProductId().equals(productId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("product image (id: {}) is not belong to product (id: {})", imageId, productId);
      return null;
    }

    ProductImageResponse productImageResponse = new ProductImageResponse(
            productImage.getId(),
            productImage.getProductId(),
            productImage.getS3BucketPath(),
            productImage.getFileName(),
            productImage.getDateCreated()
    );
    log.info("get one product image (id: {}) from product (id: {})", productImage.getId(), oldProduct.getId());
    return productImageResponse;
  }

  @DeleteMapping("/product/{productId}/image/{imageId}")
  public void deleteProductImage(@PathVariable Long productId,
                                 @PathVariable Long imageId,
                                 @AuthenticationPrincipal MyUserDetails currentUser,
                                 HttpServletResponse response)
  {
    statsDClient.incrementCounter("endpoint.product.id.image.id.delete");
    Long currentUserId = currentUser.getId();
    if(!userService.existsById(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return ;
    }

    if(!productService.existsById(productId)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      log.error("product (id: {}) not exists", productId);
      return ;
    }

    Product oldProduct = productService.getProductById(productId);
    if(!oldProduct.getOwner_user_id().equals(currentUserId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("user (id: {}) is unauthorized", currentUserId);
      return ;
    }

    if(!productImageService.existsById(imageId)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      log.error("product image (id: {}) not exists", imageId);
      return ;
    }

    ProductImage productImage = productImageService.getProductImageById(imageId);
    if(!productImage.getProductId().equals(productId)){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.error("product image (id: {}) is not belong to product (id: {})", imageId, productId);
      return ;
    }
    try {
      String s3path = productImage.getS3BucketPath();
      amazonBucketService.delete(bucketName, s3path);
      productImageService.deleteProductImageById(imageId);
      log.info("delete product image (id: {}) from product (id: {})", productImage.getId(), oldProduct.getId());
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
    catch (Exception e){
      System.out.println(e.getMessage());
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      log.error("delete product image (id: {}) from product (id: {}) failed, error => {}", productImage.getId(), oldProduct.getId(), e.getMessage());
    }
  }

}
