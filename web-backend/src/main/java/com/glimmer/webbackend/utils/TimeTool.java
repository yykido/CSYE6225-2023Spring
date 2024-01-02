package com.glimmer.webbackend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassNAME TimeTool
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/2 01:33
 * @Version 1.0
 */
public class TimeTool
{
  public static String getNowTime()
  {
    Date dt = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String currentTime = sdf.format(dt);
    return currentTime;
  }
}
