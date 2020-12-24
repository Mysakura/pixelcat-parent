package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private ZkProperties zkProperties;

    @RequestMapping("/get")
    public String get(){
        return zkProperties.getUrl() + "\n" + zkProperties.getJdbcurl();
    }
}
