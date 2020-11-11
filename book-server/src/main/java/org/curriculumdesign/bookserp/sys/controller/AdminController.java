package org.curriculumdesign.bookserp.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cdteam.spring.cloud.starter.context.base.BaseContext;
import org.cdteam.spring.cloud.starter.context.bean.R;
import org.cdteam.spring.cloud.starter.security.annotation.AnonymousAccess;
import org.curriculumdesign.bookserp.sys.dto.AdminDTO;
import org.curriculumdesign.bookserp.sys.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Api(value = "用户登录管理", tags = "[系统模块]用户登录管理")
@RestController
@RequestMapping("/v1/sys-admin")
public class AdminController extends BaseContext{

    @Autowired
    AdminService adminService;

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public AdminDTO info() {
        return adminService.getById(getUserId());
    }
}
