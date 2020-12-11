package com.pixelcat.core.config;

public interface PixelCatPropertiesConstant {

    public static final String PREFIX = "pixelcat";

    /**
     * 数据库驱动
     */
    String DRIVER = "sqlDriver";

    /**
     * 地址
     */
    String URL = "sqlUrl";

    /**
     * 用户名
     */
    String USERNAME = "sqlUsername";

    /**
     * 密码
     */
    String PASSWORD = "sqlPassword";

    /**
     * zk地址
     */
    String ZK_URL = "zkUrl";

    /**
     * 空闲时间
     */
    String ZK_SLEEP_TIME_MS = "zkSleepTimeMs";

    /**
     * 重试次数
     */
    String ZK_MAX_RETRIES = "zkMaxRetries";

    /**
     *
     * zk根目录
     */
    String ZK_ROOT_PATH = "zkRootPath";

    /**
     * 配置中心地址
     */
    String CENTER_URL = "centerUrl";

    /**
     * 客户端：项目ID
     */
    String PROJECT_ID = "projectId";

    /**
     * 客户端：环境ID
     */
    String ENV_ID = "envId";
}
