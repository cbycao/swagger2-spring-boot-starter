/**
 * Copyright: Copyright(C) 2020 Easy-Java-Rest-Framework.
 */
package io.github.swagger2.autoconfigure.properties;

import lombok.Data;


/**
 * Parameter配置项  .
 *
 * <p>
 * Parameter配置项
 *
 * @author caobaoyu
 * @date 2020/3/24 11:50
 */
@Data
public class ParameterProperties {
    private String name;
    private String description;
    private String defaultValue;
    private Boolean required;
    private Boolean allowMultiple;
    private String dataType;
    private int order;
    private String paramType;
}
