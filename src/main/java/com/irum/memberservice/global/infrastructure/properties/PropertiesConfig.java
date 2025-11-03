package com.irum.memberservice.global.infrastructure.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    JwtProperties.class,
    RedisProperties.class
})
public class PropertiesConfig {}
