package com.lyx.goods.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: xhj
 * @Date: 2023/09/23/16:42
 * @Description:
 */
@ConfigurationProperties(prefix = "xhj.thread")
// @Component
@Data
public class ThreadPoolConfigProperties {

    private Integer coreSize;

    private Integer maxSize;

    private Integer keepAliveTime;


}