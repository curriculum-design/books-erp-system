package org.curriculumdesign.bookserp.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.dto.StorageDTO;
import org.curriculumdesign.bookserp.base.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(value = "仓库管理", tags = "[基础模块]仓库管理")
@RestController
@RequestMapping("/v1/base-storage")
public class StorageController {

    @Autowired
    StorageService storageService;

    @ApiOperation("获取仓库列表")
    @GetMapping("/list")
    public Pagination<StorageDTO> info(Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum
            , String name) {
        return storageService.page(pageSize, pageNum, name);
    }

    @ApiOperation("新增仓库")
    @PostMapping("/save")
    public Integer save(@RequestBody StorageDTO storageDTO) {
        return storageService.save(storageDTO);
    }

    @ApiOperation("修改仓库")
    @PutMapping("/update")
    public Integer update(@RequestBody StorageDTO storageDTO) {
        return save(storageDTO);
    }

    @ApiOperation("删除仓库")
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Long id) {
        return storageService.delete(id);
    }

    @ApiOperation("获取图书列表")
    @GetMapping("/all")
    public List<StorageDTO> list() {
        return storageService.list();
    }

    @ApiOperation("获取图书信息")
    @GetMapping("/{id}")
    public StorageDTO get(@PathVariable Long id) {
        return storageService.getById(id);
    }
}
