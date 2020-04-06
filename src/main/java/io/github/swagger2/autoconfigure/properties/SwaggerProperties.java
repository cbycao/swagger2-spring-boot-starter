/**
 * Copyright: Copyright(C) 2020 Easy-Java-Rest-Framework.
 */
package io.github.swagger2.autoconfigure.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Swagger配置属性  .
 *
 * <p>
 * Swagger配置属性
 *
 * @author caobaoyu
 * @date 2020/3/25 17:59
 */
@ConfigurationProperties(prefix = "swagger")
@Data
public class SwaggerProperties{
    @Value("${server.servlet.context-path:/}")
    private String contextPath;
    @Value("${server.port:8080}")
    private String port;
    private boolean enabled;
    private Map<String,DocketProperties> dockets;
}
