package com.toutoleron.formapp.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("my-config")
@RefreshScope
@Getter
@Setter
public class ApplicationPropertiesConfiguration {

    private String property;
}
