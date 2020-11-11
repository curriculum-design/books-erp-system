package org.curriculumdesign.bookserp.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cdteam.spring.cloud.starter.common.utils.ListUtils;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.domain.PublisherEntity;
import org.curriculumdesign.bookserp.base.dto.PublisherDTO;
import org.curriculumdesign.bookserp.base.dto.PublisherDTO;
import org.curriculumdesign.bookserp.base.mapper.PublisherMapper;
import org.curriculumdesign.bookserp.base.service.PublisherService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 出版社 服务实现类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
@Service
public class PublisherServiceImpl implements PublisherService {

	@Autowired
    private PublisherMapper mapper;

    @Override
    public Pagination<PublisherDTO> page(Integer pageSize, Integer pageNum) {
        Page<PublisherEntity> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        IPage<PublisherEntity> bookEntityIPage = mapper.selectPage(page, Wrappers.emptyWrapper());
        List<PublisherEntity> records = bookEntityIPage.getRecords();
        List<PublisherDTO> bookDTOS = ListUtils.transferList(records, PublisherDTO.class);
        Pagination<PublisherDTO> result = new Pagination<>(pageNum, pageSize, bookEntityIPage.getTotal(), bookDTOS);
        return result;
    }
}
