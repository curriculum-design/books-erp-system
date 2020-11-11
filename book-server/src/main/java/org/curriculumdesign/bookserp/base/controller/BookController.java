package org.curriculumdesign.bookserp.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.domain.BookEntity;
import org.curriculumdesign.bookserp.base.dto.BookDTO;
import org.curriculumdesign.bookserp.base.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "图书管理", tags = "[基础模块]图书管理")
@RestController
@RequestMapping("/v1/base-book")
public class BookController {

    @Autowired
    BookService bookService;

    @ApiOperation("获取图书列表")
    @GetMapping("/list")
    public Pagination<BookDTO> info(Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum) {
        return bookService.page(pageSize, pageNum);
    }
}
