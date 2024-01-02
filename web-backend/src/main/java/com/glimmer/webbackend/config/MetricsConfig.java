package com.glimmer.webbackend.config;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassNAME MetricsConfig
 * @Description TODO
 * @Author glimmer
 * @Date 2023/3/30 21:27
 * @Version 1.0
 */
@Configuration
public class MetricsConfig {
  @Value("${publish.metrics}")
  private boolean publishMetrics;
  @Value("${metrics.server.hostname}")
  private String host;
  @Value("${metrics.server.port}")
  private int port;
  @Value("${metrics.server.prefix}")
  private String prefix;

  @Bean
  public StatsDClient statsDClient(

  ) {
    if(publishMetrics){
      return new NonBlockingStatsDClient(prefix, host, port);
    }
    return new NoOpStatsDClient();
  }
}
