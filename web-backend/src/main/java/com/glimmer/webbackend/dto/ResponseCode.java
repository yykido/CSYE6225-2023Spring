package com.glimmer.webbackend.dto;

/**
 * @ClassNAME ResponseCode
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/7 23:34
 * @Version 1.0
 */
public class ResponseCode
{
  static public final int SUCCESS = 200;
  static public final int SUCCESS_CREATE = 201;
  static public final int SUCCESS_UPDATE = 204;
  static public final int SUCCESS_EMPTY = 204;
  static public final int ERROR = 400;
  static public final int ERROR_UNAUTHORIZED = 401;
  static public final int ERROR_FORBIDDEN = 403;
  static public final int ERROR_NOT_FOUND = 404;
}
