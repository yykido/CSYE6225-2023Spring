package com.glimmer.webbackend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassNAME DataTool
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/2 03:07
 * @Version 1.0
 */
public class DataTool
{
  public static boolean checkEmailFormat(String str)
  {
    if (str == null || str.length() == 0)
    {
      return false;
    }
    String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+↵\n" +
                   ")*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(str);
    return matcher.matches();
  }
}
