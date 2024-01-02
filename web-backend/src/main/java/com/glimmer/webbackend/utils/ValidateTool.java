package com.glimmer.webbackend.utils;

import com.glimmer.webbackend.entity.Product;
import com.glimmer.webbackend.entity.User;

/**
 * @ClassNAME ValidateTool
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/8 15:57
 * @Version 1.0
 */
public class ValidateTool
{

  static public boolean isValid(User user)
  {
    return !(user.getUsername() == null ||
             user.getPassword() == null ||
             user.getFirst_name() == null ||
             user.getLast_name() == null ||
             user.getPassword().length() == 0 ||
             user.getPassword().length() > 255 ||
             user.getFirst_name().length() == 0 ||
             user.getFirst_name().length() > 255 ||
             user.getLast_name().length() == 0 ||
             user.getLast_name().length() > 255);
  }

  static public boolean isValid(Product product)
  {
    return !(product.getSku() == null ||
             product.getName() == null ||
             product.getDescription() == null ||
             product.getManufacturer() == null ||
             product.getQuantity() == null ||
             product.getSku().length() == 0 ||
             product.getSku().length() > 255 ||
             product.getName().length() == 0 ||
             product.getName().length() > 255 ||
             product.getDescription().length() == 0 ||
             product.getDescription().length() > 255 ||
             product.getManufacturer().length() == 0 ||
             product.getManufacturer().length() > 255 ||
             product.getQuantity() < 0 ||
             product.getQuantity() > 100);
  }

  static public boolean hasWrongFields(Product product)
  {
    return (product.getDate_added() != null ||
            product.getDate_last_updated() != null ||
            product.getOwner_user_id() != null);
  }

  static public boolean isEmpty(Product product)
  {
    return (product.getSku() == null &&
            product.getName() == null &&
            product.getDescription() == null &&
            product.getManufacturer() == null &&
            product.getQuantity() == null);
  }
}
