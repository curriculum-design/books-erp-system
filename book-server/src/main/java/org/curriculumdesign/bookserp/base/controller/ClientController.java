package org.curriculumdesign.bookserp.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.dto.BookTypeDTO;
import org.curriculumdesign.bookserp.base.dto.ClientDTO;
import org.curriculumdesign.bookserp.base.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(value = "客户管理", tags = "[基础模块]客户管理")
@RestController
@RequestMapping("/v1/base-client")
public class ClientController {

    @Autowired
    ClientService clientService;

    @ApiOperation("获取客户列表")
    @GetMapping("/list")
    public Pagination<ClientDTO> info(Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum
            , String name, String contactName, String contactMobile) {
        return clientService.page(pageSize, pageNum, name, contactName, contactMobile);
    }

    @ApiOperation("新增客户")
    @PostMapping("/save")
    public Integer save(@RequestBody ClientDTO clientDTO) {
        return clientService.save(clientDTO);
    }

    @ApiOperation("修改客户")
    @PutMapping("/update")
    public Integer update(@RequestBody ClientDTO clientDTO) {
        return save(clientDTO);
    }

    @ApiOperation("删除客户")
    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Long id) {
        return clientService.delete(id);
    }

    @ApiOperation("获取所有客户")
    @GetMapping("/all")
    public List<ClientDTO> list() {
        return clientService.list();
    }

    @ApiOperation("获取客户信息")
    @GetMapping("/{id}")
    public ClientDTO get(@PathVariable Long id) {
        return clientService.getById(id);
    }
}