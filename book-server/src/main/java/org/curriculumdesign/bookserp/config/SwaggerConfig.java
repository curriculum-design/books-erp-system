package org.curriculumdesign.bookserp.config;

import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelSpecification;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Configuration
@EnableSwagger2
@ConfigurationProperties("swagger")
public class SwaggerConfig {
    private String title;
    private String version;
    private String des;

    @Bean
    public Docket api(ServletContext servletContext) {
        List<RequestParameter> parameters = new ArrayList<>();

        parameters.add(new RequestParameterBuilder()
                .name("x-token")
                .description("请求TOKEN")
                .required(Boolean.FALSE)
                .in(ParameterType.HEADER)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .required(Boolean.FALSE)
                .build());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalRequestParameters(parameters)
                .directModelSubstitute(Timestamp.class, Long.class)
                .directModelSubstitute(Date.class, Long.class)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }


    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(title).description(des).version(version)
                .build();
    }
}
