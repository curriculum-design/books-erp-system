package org.curriculumdesign.bookserp.common.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.cdteam.spring.cloud.starter.security.annotation.AnonymousAccess;
import org.curriculumdesign.bookserp.common.model.TableInfoEntity;
import org.curriculumdesign.bookserp.common.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;

/**
 * Created by Diamond on 2017/7/19.
 */
@ApiIgnore
@RestController
@RequestMapping("common")
@Validated
public class CommonController {

    @Autowired
    CommonService commonService;

    @AnonymousAccess
    @ApiOperation("获取表字段名以及注解")
    @ApiImplicitParam(name = "table", paramType = "query", required = true)
    @GetMapping("table-fields")
    public TableInfoEntity getTableFields(@NotNull String table) {
        return commonService.selectTableFields(table);
    }


}
