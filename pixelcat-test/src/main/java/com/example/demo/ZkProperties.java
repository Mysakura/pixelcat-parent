package com.example.demo;

import com.pixelcat.core.config.PixelCatConfig;
import com.pixelcat.core.config.PixelCatValue;
import lombok.Getter;
import org.springframework.stereotype.Component;

@PixelCatConfig(namespace = "zk.properties")
@Component
@Getter
public class ZkProperties {

    @PixelCatValue(key = "url")
    private String url;

    @PixelCatValue(key = "db.config.datasource.pub.jdbcurl")
    private String jdbcurl;
}
