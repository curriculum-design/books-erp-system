package org.curriculumdesign.bookserp.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cdteam.spring.cloud.starter.common.utils.ListUtils;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.domain.BookEntity;
import org.curriculumdesign.bookserp.base.dto.BookDTO;
import org.curriculumdesign.bookserp.base.mapper.BookMapper;
import org.curriculumdesign.bookserp.base.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper mapper;

    @Override
    public Pagination<BookDTO> page(Integer pageSize, Integer pageNum) {
        Page<BookEntity> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        IPage<BookEntity> bookEntityIPage = mapper.selectPage(page, Wrappers.emptyWrapper());
        List<BookEntity> records = bookEntityIPage.getRecords();
        List<BookDTO> bookDTOS = ListUtils.transferList(records, BookDTO.class);
        Pagination<BookDTO> result = new Pagination<>(pageNum, pageSize, bookEntityIPage.getTotal(), bookDTOS);
        return result;
    }
}
