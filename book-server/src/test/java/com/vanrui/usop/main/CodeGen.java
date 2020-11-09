package com.vanrui.usop.main;

import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.vanrui.spring.boot.starter.autocode.CodeGenerator;
import com.vanrui.spring.boot.starter.autocode.config.GeneratorConfig;

import java.util.Map;
import java.util.Set;

public class CodeGen {

    public static final String jdbcUrl = "jdbc:mysql://47.106.129.111:3306/books?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false";

    public static void main(String[] args) throws Exception {
        String userDir = System.getProperty("user.dir");
        System.setProperty("user.dir", "C:\\vanruiproject\\books-erp-system\\book-server");
        CodeGenerator cg = buildSystemGenerate();
        cg.generate();
    }

    public static CodeGenerator buildSystemGenerate() {
        return new CodeGenerator(new GeneratorConfig() {
            @Override
            public String tablePrefix() {
                return "base";
            }

            @Override
            public String[] tableNames() {
                return new String[]{
                        "base_supplier",
                };
            }

            @Override
            public String projectPath() {
                return null;
            }

            @Override
            public Map<String, Set<FileType>> recoveryTables() {
//                return ImmutableMap.of();
                return ImmutableMap.of("base_supplier", ImmutableSet.of(FileType.ENTITY, FileType.MAPPER, FileType.XML));
            }

            @Override
            public String parentPackage() {
                return "org.curriculumdesign.bookserp.base";
            }

            @Override
            public String dsUrl() {
                return jdbcUrl;
            }

            @Override
            public String dsUserName() {
                return "root";
            }

            @Override
            public String dsPassword() {
                return "ue88824568==321";
            }

            @Override
            public String dsDriverName() {
                return "com.mysql.jdbc.Driver";
            }

            @Override
            public String author() {
                return "lesl";
            }
        });
    }

}
