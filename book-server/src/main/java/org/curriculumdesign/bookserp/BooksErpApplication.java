package org.curriculumdesign.bookserp;

import org.cdteam.spring.cloud.starter.web.EnableCdteamWeb;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Repository;

@EnableScheduling
@EnableFeignClients
@SpringBootApplication
@MapperScan(basePackages = {"org.curriculumdesign.bookserp.mapper", "org.curriculumdesign.bookserp.*.mapper"}, annotationClass = Repository.class)
@EnableCdteamWeb(basePackages = {"org.curriculumdesign.bookserp.*"})
public class BooksErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksErpApplication.class, args);
    }
}
