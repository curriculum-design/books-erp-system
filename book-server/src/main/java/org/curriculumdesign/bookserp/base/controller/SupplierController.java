package org.curriculumdesign.bookserp.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.dto.BookTypeDTO;
import org.curriculumdesign.bookserp.base.dto.SupplierDTO;
import org.curriculumdesign.bookserp.base.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(value = "供应商管理", tags = "[基础模块]供应商管理")
@RestController
@RequestMapping("/v1/base-supplier")
public class SupplierController {

    @Autowired
    SupplierService supplierService;

    @ApiOperation("获取供应商列表")
    @GetMapping("/list")
    public Pagination<SupplierDTO> info(Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum
            , String name, String code, String contactName, String contactMobile) {
        return supplierService.page(pageSize, pageNum, name, code, contactName, contactMobile);
    }

    @ApiOperation("新增供应商")
    @PostMapping("/save")
    public Integer save(@RequestBody SupplierDTO supplierDTO) {
        return supplierService.save(supplierDTO);
    }

    @ApiOperation("修改供应商")
    @PutMapping("/update")
    public Integer update(@RequestBody SupplierDTO supplierDTO) {
        return save(supplierDTO);
    }

    @ApiOperation("删除供应商")
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Long id) {
        return supplierService.delete(id);
    }

    @ApiOperation("获取所有供应商")
    @GetMapping("/all")
    public List<SupplierDTO> list() {
        return supplierService.list();
    }

    @ApiOperation("获取供应商信息")
    @GetMapping("/{id}")
    public SupplierDTO get(@PathVariable Long id) {
        return supplierService.getById(id);
    }
}