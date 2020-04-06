/**
 * Copyright: Copyright(C) 2020 Easy-Java-Rest-Framework.
 */
package io.github.swagger2.autoconfigure.properties;

import lombok.Data;

/**
 * ApiInfo配置项  .
 *
 * <p>
 * ApiInfo配置项
 *
 * @author caobaoyu
 * @date 2020/3/24 11:46
 */
@Data
public class ApiInfoProperties {

    private String version;
    private String title;
    private String description;
    private String termsOfServiceUrl;
    private String license;
    private String licenseUrl;
    private ContactProperties contact;
}
