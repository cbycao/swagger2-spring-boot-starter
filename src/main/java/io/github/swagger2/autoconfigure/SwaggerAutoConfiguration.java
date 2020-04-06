/**
 * Copyright: Copyright(C) 2020 Easy-Java-Rest-Framework.
 */

package io.github.swagger2.autoconfigure;

import io.github.swagger2.autoconfigure.properties.*;
import io.github.swagger2.autoconfigure.utils.Utils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import springfox.documentation.RequestHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Function;

/**
 * Swagger自动配置类  .
 *
 * <p>
 * Swagger自动配置类
 *
 * @author caobaoyu
 * @date 2020/3/24 13:35
 */
@Configuration
@ConditionalOnClass(Docket.class)
@EnableSwagger2
@EnableConfigurationProperties({SwaggerProperties.class})
@ConditionalOnProperty(
        value = {"swagger.enabled"},
        havingValue = "true",
        matchIfMissing = true
)
class SwaggerAutoConfiguration implements BeanFactoryAware {
    private static final String PACKAGE_SPLIT = ";";
    @Autowired
    private SwaggerProperties swaggerProperties;

    /**
     * 动态注入swagger配置
     *
     * <p>
     * 动态注入swagger配置
     *
     * @param beanFactory BeanFactory
     * @Execption BeansException
     * @author caobaoyu
     * @date 2020/4/5 0:41
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        if (swaggerProperties != null && swaggerProperties.getDockets() != null) {
            swaggerProperties.getDockets().forEach(
                    (key, value) -> {
                        Docket docket = createDocket(value);
                        if (docket != null) {
                            listableBeanFactory.registerSingleton(key, docket);
                        }
                    }
            );
        }
    }

    /**
     *  创建Docket.
     *
     *  <p>
     * 创建Docket
     *
     * @param docketProperties 配置
     *
     * @return springfox.documentation.spring.web.plugins.Docket  Docket
     *
     * @author caobaoyu
     * @date 2020/4/5 0:43
     */
    private Docket createDocket(DocketProperties docketProperties) {
        Docket docket = null;
        docket = new Docket(DocumentationType.SWAGGER_2);
        if (docketProperties.getGroupName() != null) {
            docket.groupName(docketProperties.getGroupName());
        }

        ApiInfo apiInfo = createApiInfo(docketProperties.getApiInfo());
        if (apiInfo != null) {
            docket.apiInfo(apiInfo);
        }
        docket.pathMapping("/");
        ApiSelectorBuilder apiSelectorBuilder = docket.select();
        if (docketProperties.getBasePackages() != null) {
            try {
                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                List<String> packageList = Utils.getPackagePaths(classLoader,docketProperties.getBasePackages());
                String packageListStr = String.join(PACKAGE_SPLIT, packageList);
                apiSelectorBuilder.apis(basePackage(packageListStr));
            }catch (Exception e){

            }
        }
        apiSelectorBuilder.paths(PathSelectors.any());
        apiSelectorBuilder.build();
        docket.globalOperationParameters(createParameterList(docketProperties.getParameters()));
        List<ResponseMessage> responseMessageList = createResponseMessageList(docketProperties.getResponses());
        docket.globalResponseMessage(RequestMethod.POST, responseMessageList);
        docket.globalResponseMessage(RequestMethod.GET, responseMessageList);
        docket.globalResponseMessage(RequestMethod.PUT, responseMessageList);
        docket.globalResponseMessage(RequestMethod.OPTIONS, responseMessageList);
        docket.globalResponseMessage(RequestMethod.DELETE, responseMessageList);

        return docket;
    }

    /**
     * 创建 ApiInfo.
     *
     *  <p>
     * 创建 ApiInfo
     *
     * @param apiInfoProperties 配置
     *
     * @return springfox.documentation.service.ApiInfo ApiInfo
     *
     * @author caobaoyu
     * @date 2020/4/5 0:44
     */
    private ApiInfo createApiInfo(ApiInfoProperties apiInfoProperties) {
        ApiInfo apiInfo = null;
        if (apiInfoProperties != null) {
            Contact contact = createContact(apiInfoProperties.getContact());
            apiInfo = new ApiInfo(apiInfoProperties.getTitle(), apiInfoProperties.getDescription(), apiInfoProperties.getVersion(), apiInfoProperties.getTermsOfServiceUrl(), contact, apiInfoProperties.getLicense(), apiInfoProperties.getLicenseUrl(), new ArrayList());
        }
        return apiInfo;
    }

    /**
     * 创建联系人 .
     *
     *  <p>
     * 创建联系人
     *
     * @param contactProperties 配置
     *
     * @return springfox.documentation.service.Contact  Contact
     *
     * @author caobaoyu
     * @date 2020/4/5 0:45
     */
    private Contact createContact(ContactProperties contactProperties) {
        Contact contact = null;
        if (contactProperties != null) {
            contact = new Contact(contactProperties.getName(), contactProperties.getUrl(), contactProperties.getEmail());
        }
        return contact;
    }

    /**
     * 创建全局参数 .
     *
     *  <p>
     * 创建全局参数
     *
     * @param parameterPropertiesList 配置
     *
     * @return java.util.List<springfox.documentation.service.Parameter> Parameter List
     *
     * @author caobaoyu
     * @date 2020/4/5 0:46
     */
    private List<Parameter> createParameterList(List<ParameterProperties> parameterPropertiesList) {
        List<Parameter> parameterList = new ArrayList<>();
        if (parameterPropertiesList != null) {
            for (ParameterProperties parameterProperties : parameterPropertiesList) {
                ParameterBuilder parameterBuilder = new ParameterBuilder();
                parameterBuilder.name(parameterProperties.getName())
                        .description(parameterProperties.getDescription())
                        .modelRef(new ModelRef(parameterProperties.getDataType()))
                        .parameterType(parameterProperties.getParamType())
                        .required(parameterProperties.getRequired())
                        //.order(parameterProperties.getOrder())
                        .defaultValue(parameterProperties.getDefaultValue());
                parameterList.add(parameterBuilder.build());
            }
        }
        return parameterList;
    }

    /**
     * 创建全局响应信息 .
     *
     *  <p>
     * 创建全局响应信息
     *
     * @param responsePropertiesList 配置
     *
     * @return java.util.List<springfox.documentation.service.ResponseMessage> ResponseMessage List
     *
     * @author caobaoyu
     * @date 2020/4/5 0:48
     */
    private List<ResponseMessage> createResponseMessageList(List<ResponseProperties> responsePropertiesList) {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        if (responsePropertiesList != null) {
            for (ResponseProperties responseProperties : responsePropertiesList) {
                ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder();
                responseMessageBuilder.code(responseProperties.getCode()).message(responseProperties.getMessage());
                if (responseProperties.getModelRef() != null && !"".equals(responseProperties.getModelRef())) {
                    ModelRef modelRef = new ModelRef(responseProperties.getModelRef());
                    responseMessageBuilder.responseModel(modelRef);
                }
                responseMessageList.add(responseMessageBuilder.build());
            }
        }
        return responseMessageList;
    }

    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            for (String strPackage : basePackage.split(PACKAGE_SPLIT)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

}
