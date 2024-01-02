package com.glimmer.webbackend.controller;

import com.timgroup.statsd.StatsDClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassNAME PublicController
 * @Description TODO
 * @Author glimmer
 * @Date 2023/2/1 18:52
 * @Version 1.0
 */
@RestController
@RequestMapping("/healthz")
@Slf4j
public class PublicController
{
  @Autowired
  private StatsDClient statsDClient;

  @GetMapping("")
  public String health()
  {
    statsDClient.incrementCounter("endpoint.healthz.get");
    log.info("get healthz check" );
    return "ok";
  }
}
