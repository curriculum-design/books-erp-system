package org.curriculumdesign.bookserp.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.dto.BookTypeDTO;
import org.curriculumdesign.bookserp.base.service.BookTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(value = "图书类型管理", tags = "[基础模块]图书类型管理")
@RestController
@RequestMapping("/v1/base-book-type")
public class BookTypeController {

    @Autowired
    BookTypeService bookTypeService;

    @ApiOperation("获取图书类型列表")
    @GetMapping("/list")
    public Pagination<BookTypeDTO> info(Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum
            , String name) {
        return bookTypeService.page(pageSize, pageNum, name);
    }

    @ApiOperation("新增图书类型")
    @PostMapping("/save")
    public Integer save(@RequestBody BookTypeDTO bookTypeDTO) {
        return bookTypeService.save(bookTypeDTO);
    }

    @ApiOperation("修改图书类型")
    @PutMapping("/update")
    public Integer update(@RequestBody BookTypeDTO bookTypeDTO) {
        return save(bookTypeDTO);
    }

    @ApiOperation("删除图书类型")
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Long id) {
        return bookTypeService.delete(id);
    }

    @ApiOperation("获取图书列表")
    @GetMapping("/all")
    public List<BookTypeDTO> list() {
        return bookTypeService.list();
    }

    @ApiOperation("获取图书信息")
    @GetMapping("/{id}")
    public BookTypeDTO get(@PathVariable Long id) {
        return bookTypeService.getById(id);
    }
}
