/**
 * Copyright: Copyright(C) 2020 Easy-Java-Rest-Framework.
 */
package io.github.swagger2.autoconfigure.properties;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Docket配置  .
 *
 * <p>
 * Docket配置
 *
 * @author caobaoyu
 * @date 2020/3/24 13:01
 */
@Data
public class DocketProperties {
    private String pathMapping;
    private String host;
    private String groupName;
    private List<String> basePackages;
    private Set<String> protocols =  Stream.of("http", "https").collect(Collectors.toSet());
    private Set<String> produces =  Stream.of("application/json").collect(Collectors.toSet());
    private Set<String> consumes = Stream.of("application/json").collect(Collectors.toSet());
    private ApiInfoProperties apiInfo;
    private List<ParameterProperties> parameters = new ArrayList();
    private List<ResponseProperties> responses = new ArrayList();
}
