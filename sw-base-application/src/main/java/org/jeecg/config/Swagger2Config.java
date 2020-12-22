package org.jeecg.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.shiro.vo.DefContants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author scott
 */
@Slf4j
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class Swagger2Config implements WebMvcConfigurer {

    /**
     * 显示swagger-ui.html文档展示页，还必须注入swagger资源：
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
     *
     * @return Docket
     */
    @Bean
    public Docket createRestApi() {
        //		+splitor+"org.jeecg.snow.store.modules"
        return new Docket(DocumentationType.SWAGGER_2).groupName("公共模块").apiInfo(apiInfo()).select()
            //此包路径下的类，才生成接口文档
            .apis(RequestHandlerSelectors.basePackage("org.jeecg.modules"))
            //加了ApiOperation注解的类，才生成接口文档
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any()).build()
            //.securitySchemes(securityScheme());
            .globalOperationParameters(setHeaderToken());
    }

    @Bean
    public Docket createRestApi2() {
        //		+splitor+"org.jeecg.snow.store.modules"
        return new Docket(DocumentationType.SWAGGER_2).groupName("信息化系统").apiInfo(apiInfo()).select()
            //此包路径下的类，才生成接口文档
            .apis(RequestHandlerSelectors.basePackage("org.jeecg.sw.manage.modules"))
            //加了ApiOperation注解的类，才生成接口文档
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any()).build()
            //.securitySchemes(securityScheme()).securityContexts(securityContexts());
            .globalOperationParameters(setHeaderToken());
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
            SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    /***
     * oauth2配置
     * 需要增加swagger授权回调地址
     * http://localhost:8888/webjars/springfox-swagger-ui/o2c.html
     * @return
     */

    private List<ApiKey> securityScheme() {
        List<ApiKey> list = new ArrayList<ApiKey>();
        ApiKey apiKey = new ApiKey("oauth2 认证", DefContants.X_ACCESS_TOKEN, "header");
        list.add(apiKey);
        return list;
    }

    /**
     * JWT token
     *
     * @return
     */
    private List<Parameter> setHeaderToken() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name(DefContants.X_ACCESS_TOKEN).description("token").modelRef(new ModelRef("string"))
            .parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }

    /**
     * api文档的详细信息函数,注意这里的注解引用的是哪个
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            // //大标题
            .title("Sw 后台服务API接口文档")
            // 版本号
            .version("1.0")
            //				.termsOfServiceUrl("NO terms of service")
            // 描述
            .description("后台API接口")
            // 作者
            .contact(new Contact("Sw团队", "www.Sw.com", "")).build();
    }

}