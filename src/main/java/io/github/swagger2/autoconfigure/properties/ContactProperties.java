/**
 * Copyright: Copyright(C) 2020 Easy-Java-Rest-Framework.
 */

package io.github.swagger2.autoconfigure.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  Contact配置项  .
 *
 * <p>
 * Contact配置项
 *
 * @author caobaoyu
 * @date 2020/3/25 22:08
 */
@Data
public class ContactProperties {
    private String name;
    private String url;
    private String email;
}

