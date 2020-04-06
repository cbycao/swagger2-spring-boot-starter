/**
 * Copyright: Copyright(C) 2020 Easy-Java-Rest-Framework.
 */

package io.github.swagger2.autoconfigure.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response配置  .
 *
 * <p>
 * Response配置
 *
 * @author caobaoyu
 * @date 2020/3/24 11:53
 */
@Data
public class ResponseProperties {
    private int code;
    private String message;
    private String modelRef;
}
