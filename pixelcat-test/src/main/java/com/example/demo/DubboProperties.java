package com.example.demo;

import com.pixelcat.core.config.PixelCatConfig;
import com.pixelcat.core.config.PixelCatValue;
import lombok.Getter;
import org.springframework.stereotype.Component;

@PixelCatConfig(namespace = "dubbo.properties")
@Component
@Getter
public class DubboProperties {

    @PixelCatValue(key = "dubbo.url")
    private String url;

    @PixelCatValue(key = "dubbo.port")
    private String port;

}
