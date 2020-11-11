package org.curriculumdesign.bookserp.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.domain.BookEntity;
import org.curriculumdesign.bookserp.base.dto.BookDTO;
import org.curriculumdesign.bookserp.base.dto.BookDTO;
import org.curriculumdesign.bookserp.base.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(value = "图书管理", tags = "[基础模块]图书管理")
@RestController
@RequestMapping("/v1/base-book")
public class BookController {

    @Autowired
    BookService bookService;

    @ApiOperation("获取图书列表")
    @GetMapping("/list")
    public Pagination<BookDTO> info(Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum
            , Long typeId, Long publisherId, String code, String name) {
        return bookService.page(pageSize, pageNum, typeId, publisherId, code, name);
    }

    @ApiOperation("新增图书")
    @PostMapping("/save")
    public Integer save(@RequestBody BookDTO bookDTO) {
        return bookService.save(bookDTO);
    }

    @ApiOperation("修改图书")
    @PutMapping("/update")
    public Integer update(@RequestBody BookDTO bookDTO) {
        return save(bookDTO);
    }

    @ApiOperation("删除图书")
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Long id) {
        return bookService.delete(id);
    }

    @ApiOperation("获取所有图书")
    @GetMapping("/all")
    public List<BookDTO> list() {
        return bookService.list();
    }

    @ApiOperation("获取图书信息")
    @GetMapping("/{id}")
    public BookDTO get(@PathVariable Long id) {
        return bookService.getById(id);
    }
}
