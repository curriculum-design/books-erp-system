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
import org.curriculumdesign.bookserp.base.domain.BookEntity;
import org.curriculumdesign.bookserp.base.domain.BookEntity;
import org.curriculumdesign.bookserp.base.dto.BookDTO;
import org.curriculumdesign.bookserp.base.mapper.BookMapper;
import org.curriculumdesign.bookserp.base.service.BookService;
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
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper mapper;

    @Override
    public Pagination<BookDTO> page(Integer pageSize, Integer pageNum, Long typeId, Long publisherId, String code, String name) {
        Page<BookEntity> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<BookEntity> queryWrapper = Wrappers.lambdaQuery();
        if (Objects.nonNull(typeId)) {
            queryWrapper.eq(BookEntity::getTypeId, typeId);
        }
        if (Objects.nonNull(publisherId)) {
            queryWrapper.eq(BookEntity::getPublisherId, publisherId);
        }
        if (StringUtils.isNotBlank(code)) {
            queryWrapper.like(BookEntity::getCode, code);
        }
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(BookEntity::getName, name);
        }
        IPage<BookEntity> bookEntityIPage = mapper.selectPage(page, queryWrapper);
        List<BookEntity> records = bookEntityIPage.getRecords();
        List<BookDTO> bookDTOS = ListUtils.transferList(records, BookDTO.class);
        Pagination<BookDTO> result = new Pagination<>(pageNum, pageSize, bookEntityIPage.getTotal(), bookDTOS);
        return result;
    }

    @Override
    public Integer save(BookDTO bookDTO) {
        if (isExistByName(bookDTO.getName(), bookDTO.getId())) {
            throw new AppException(ResponseCodeEnum.COMMON_EXCEPTION, "名称已存在");
        }
        if (isExistByCode(bookDTO.getCode(), bookDTO.getId())) {
            throw new AppException(ResponseCodeEnum.COMMON_EXCEPTION, "编号已存在");
        }
        BookEntity bookEntity = BeanCopyUtils.transferBean(bookDTO, BookEntity.class);
        if (bookDTO.getId() == null) {
            return mapper.insert(bookEntity);
        }
        return mapper.updateById(bookEntity);
    }

    @Override
    public Integer delete(Long id) {
        return mapper.deleteById(id);
    }

    @Override
    public List<BookDTO> list() {
        List<BookEntity> records = mapper.selectList(Wrappers.emptyWrapper());
        List<BookDTO> bookDTOS = ListUtils.transferList(records, BookDTO.class);
        return bookDTOS;
    }

    @Override
    public BookDTO getById(Long id) {
        BookEntity entity = mapper.selectById(id);
        return BeanCopyUtils.transferBean(entity, BookDTO.class);
    }

    private boolean isExistByName(String name, Long id) {
        LambdaQueryWrapper<BookEntity> queryWrapper = Wrappers.<BookEntity>lambdaQuery().eq(BookEntity::getName, name);
        if (Objects.nonNull(id)) {
            queryWrapper.ne(BookEntity::getId, id);
        }
        return mapper.selectCount(queryWrapper) > 0;
    }
    private boolean isExistByCode(String code, Long id) {
        LambdaQueryWrapper<BookEntity> queryWrapper = Wrappers.<BookEntity>lambdaQuery().eq(BookEntity::getCode, code);
        if (Objects.nonNull(id)) {
            queryWrapper.ne(BookEntity::getId, id);
        }
        return mapper.selectCount(queryWrapper) > 0;
    }
}
