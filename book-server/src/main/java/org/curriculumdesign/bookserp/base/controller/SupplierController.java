package org.curriculumdesign.bookserp.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.curriculumdesign.bookserp.base.dto.SupplierDTO;
import org.curriculumdesign.bookserp.base.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lesl
 */
@Slf4j
@Api(value = "供应商管理", tags = "[基础模块]供应商管理")
@RestController
@RequestMapping("/base/supplier")
public class SupplierController {

    @Autowired
    SupplierService supplierService;

    @ApiOperation("供应商列表")
    @GetMapping(value = "/list")
    public List<SupplierDTO> supplierList() {
        return supplierService.findAll();
    }
}
