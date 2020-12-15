package com.example.demo;

import com.pixelcat.core.config.PixelCatConfig;
import com.pixelcat.core.config.PixelCatValue;
import org.springframework.stereotype.Component;

@PixelCatConfig(namespace = "zk.Properties")
@Component
public class ZkProperties {

    @PixelCatValue(key = "url")
    private String url;
}
