package com.unindra.ngrancang.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@ConfigurationProperties(prefix = "env.data")
@Configuration("envProperties")
@Data
public class EnvConfig {
    private String secret;
}
