package org.curriculumdesign.bookserp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.cdteam.spring.cloud.starter.common.utils.BeanCopyUtils;
import org.cdteam.spring.cloud.starter.common.utils.ListUtils;
import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.cdteam.spring.cloud.starter.context.constant.ResponseCodeEnum;
import org.cdteam.spring.cloud.starter.context.exception.AppException;
import org.curriculumdesign.bookserp.base.domain.BookTypeEntity;
import org.curriculumdesign.bookserp.base.dto.BookTypeDTO;
import org.curriculumdesign.bookserp.base.mapper.BookTypeMapper;
import org.curriculumdesign.bookserp.base.service.BookTypeService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
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
    public Pagination<BookTypeDTO> page(Integer pageSize, Integer pageNum, String name) {
        Page<BookTypeEntity> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<BookTypeEntity> queryWrapper = Wrappers.<BookTypeEntity>lambdaQuery();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(BookTypeEntity::getName, name);
        }
        IPage<BookTypeEntity> bookEntityIPage = mapper.selectPage(page, queryWrapper);
        List<BookTypeEntity> records = bookEntityIPage.getRecords();
        List<BookTypeDTO> bookDTOS = ListUtils.transferList(records, BookTypeDTO.class);
        Pagination<BookTypeDTO> result = new Pagination<>(pageNum, pageSize, bookEntityIPage.getTotal(), bookDTOS);
        return result;
    }


    @Override
    public int save(BookTypeDTO publisherDTO) {
        if (isExist(publisherDTO.getName(), publisherDTO.getId())) {
            throw new AppException(ResponseCodeEnum.COMMON_EXCEPTION, "名称已存在");
        }
        BookTypeEntity bookTypeEntity = BeanCopyUtils.transferBean(publisherDTO, BookTypeEntity.class);
        if (publisherDTO.getId() == null) {
            return mapper.insert(bookTypeEntity);
        }
        return mapper.updateById(bookTypeEntity);
    }

    @Override
    public Integer delete(Long id) {
        return mapper.deleteById(id);
    }

    @Override
    public List<BookTypeDTO> list() {
        List<BookTypeEntity> records = mapper.selectList(Wrappers.emptyWrapper());
        List<BookTypeDTO> bookDTOS = ListUtils.transferList(records, BookTypeDTO.class);
        return bookDTOS;
    }

    @Override
    public BookTypeDTO getById(Long id) {
        BookTypeEntity bookTypeEntity = mapper.selectById(id);
        return BeanCopyUtils.transferBean(bookTypeEntity, BookTypeDTO.class);
    }

    private boolean isExist(String name, Long id) {
        LambdaQueryWrapper<BookTypeEntity> queryWrapper = Wrappers.<BookTypeEntity>lambdaQuery().eq(BookTypeEntity::getName, name);
        if (Objects.nonNull(id)) {
            queryWrapper.ne(BookTypeEntity::getId, id);
        }
        return mapper.selectCount(queryWrapper) > 0;
    }
}
