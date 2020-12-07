package com.pixelcat.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("pixelcat")
@Data
public class PixelCatProperties {


    /**
     * 数据库驱动
     */
    private String driver = "com.mysql.jdbc.Driver";

    /**
     * 地址
     */
    private String url = "jdbc:mysql://10.20.26.47:3306/lcptpub?characterEncoding=utf8";

    /**
     * 用户名
     */
    private String username = "root";

    /**
     * 密码
     */
    private String password = "handsome";

    /**
     * zk地址
     */
    private String zkUrl = "127.0.0.1:2181";

    /**
     * 空闲时间
     */
    private Integer zkSleepTimeMs = 1000;

    /**
     * 重试次数
     */
    private Integer zkMaxRetries = 3;

    /**
     *
     * zk根目录
     */
    private String zkRootPath = "/pixelcat";

    /**
     * 配置中心地址
     */
    private String centerUrl = "http://127.0.0.1:9000";


}
