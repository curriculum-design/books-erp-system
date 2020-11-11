package org.curriculumdesign.bookserp.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.dto.BookTypeDTO;
import org.curriculumdesign.bookserp.base.dto.PublisherDTO;
import org.curriculumdesign.bookserp.base.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(value = "出版社管理", tags = "[基础模块]出版社管理")
@RestController
@RequestMapping("/v1/base-publisher")
public class PublisherController {

    @Autowired
    PublisherService publisherService;

    @ApiOperation("获取出版社列表")
    @GetMapping("/list")
    public Pagination<PublisherDTO> info(Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum
            , String name, String contactName, String contactMobile) {
        return publisherService.page(pageSize, pageNum, name, contactName, contactMobile);
    }

    @ApiOperation("新增出版社")
    @PostMapping("/save")
    public Integer save(@RequestBody PublisherDTO publisherDTO) {
        return publisherService.save(publisherDTO);
    }

    @ApiOperation("修改出版社")
    @PutMapping("/update")
    public Integer update(@RequestBody PublisherDTO publisherDTO) {
        return save(publisherDTO);
    }

    @ApiOperation("删除出版社")
    @DeleteMapping("/delete")
    public Integer save(Long id) {
        return publisherService.delete(id);
    }

    @ApiOperation("获取出版社列表")
    @GetMapping("/all")
    public List<PublisherDTO> list() {
        return publisherService.list();
    }

    @ApiOperation("获取出版社信息")
    @GetMapping("/{id}")
    public PublisherDTO get(@PathVariable Long id) {
        return publisherService.getById(id);
    }
}