package com.pixelcat.spring.boot.autoconfigure;

import com.pixelcat.core.config.PixelCatPropertiesConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(PixelCatPropertiesConstant.PREFIX)
@Data
public class PixelCatProperties {
    public static final String BEAN_NAME = "pixelCatProperties";


    /**
     * 数据库驱动
     */
    private String driver = "com.mysql.jdbc.Driver";

    /**
     * 地址
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * zk地址
     */
    private String zkUrl = "127.0.0.1:2181";

    /**
     * 空闲时间
     */
    private String zkSleepTimeMs = "1000";

    /**
     * 重试次数
     */
    private String zkMaxRetries = "3";

    /**
     *
     * zk根目录
     */
    private String zkRootPath = "/pixelcat";

    /**
     * 配置中心地址
     */
    @Deprecated
    private String centerUrl = "http://127.0.0.1:8888";

    /**
     * 客户端：项目ID
     */
    private String projectId;

    /**
     * 客户端：环境ID
     */
    private String envId;

}
