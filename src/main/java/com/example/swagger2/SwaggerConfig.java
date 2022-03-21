package com.example.swagger2;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p> Swagger2配置 </p>
 * demo地址： http://localhost:9191/swagger-ui.html
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/7
 * </pre>
 *
 * from:  https://blog.csdn.net/u012702547/article/details/88775298
 */
@Configuration
@EnableSwagger2
// 使用注解 @ConditionalOnProperty  对暴露api进行开关控制。
@ConditionalOnProperty(prefix = "swagger",value = {"enable"},havingValue = "true")
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        /*
         * 可以采用 ‘包含注解’ 的方式来确定要显示的接口
         * apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
         *
         * 还可以采用 ‘包扫描’  的方式来确定要显示的接口
         * apis(RequestHandlerSelectors.basePackage("com.example.controller"))
         *
         * 这里采用 注册方式。
         */
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.controller"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("标题演示：SpringBoot整合Swagger")
                .description("描述：SpringBoot整合Swagger，详细文档信息......")
                .contact(
                        new Contact(
                        "内容详情：链接1 demo，CSDN博客地址，邮箱",
                        "http://blog.csdn.net",
                        "mrwxb@foxmail.com")
                )
                .version("版本号：1.0")
                .license("版权信息：链接2 demo，The Apache License").licenseUrl("http://www.baidu.com")
                .build();
    }


}
