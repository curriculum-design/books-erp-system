package org.curriculumdesign.bookserp.config;

import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.sql.Timestamp;
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
//
//        List<Parameter> parameters = ImmutableList.of(
//                new ParameterBuilder().parameterType("header").name("x-token").required(true).description("X-TOKEN")
//                        .modelRef(new ModelRef("string")).required(false).build()
//        );
        return new Docket(DocumentationType.SWAGGER_2)
//                .globalOperationParameters(parameters)
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
