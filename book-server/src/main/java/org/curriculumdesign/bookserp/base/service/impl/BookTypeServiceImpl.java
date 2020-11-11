package org.curriculumdesign.bookserp.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cdteam.spring.cloud.starter.common.utils.ListUtils;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.domain.BookTypeEntity;
import org.curriculumdesign.bookserp.base.dto.BookTypeDTO;
import org.curriculumdesign.bookserp.base.dto.BookTypeDTO;
import org.curriculumdesign.bookserp.base.mapper.BookTypeMapper;
import org.curriculumdesign.bookserp.base.service.BookTypeService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
@Service
public class BookTypeServiceImpl implements BookTypeService {

	@Autowired
    private BookTypeMapper mapper;

    @Override
    public Pagination<BookTypeDTO> page(Integer pageSize, Integer pageNum) {
        Page<BookTypeEntity> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        IPage<BookTypeEntity> bookEntityIPage = mapper.selectPage(page, Wrappers.emptyWrapper());
        List<BookTypeEntity> records = bookEntityIPage.getRecords();
        List<BookTypeDTO> bookDTOS = ListUtils.transferList(records, BookTypeDTO.class);
        Pagination<BookTypeDTO> result = new Pagination<>(pageNum, pageSize, bookEntityIPage.getTotal(), bookDTOS);
        return result;
    }
}
