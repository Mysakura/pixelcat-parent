package com.example.demo;

import com.pixelcat.core.zk.handle.ConfigHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private ZkProperties zkProperties;

    @Autowired
    private DubboProperties dubboProperties;

    @Autowired
    private ConfigHandler configHandler;

    @RequestMapping("/get")
    public String get(){
        return zkProperties.getUrl() + "\n" + zkProperties.getJdbcurl();
    }

    @RequestMapping("/getD")
    public String getD(){
        return dubboProperties.getUrl() + "\n" + dubboProperties.getPort();
    }

    @RequestMapping("/list")
    public String list(){
        return configHandler.listPath();
    }
}
