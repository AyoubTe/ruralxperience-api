package com.ruralxperience.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
@Getter
@Setter
public class StorageProperties {
    private String type;
    private String localDir;
    private String s3Bucket;
    private String s3Region;
    private String cdnBaseUrl;
}

