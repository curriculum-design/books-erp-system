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
import org.curriculumdesign.bookserp.base.domain.PublisherEntity;
import org.curriculumdesign.bookserp.base.dto.PublisherDTO;
import org.curriculumdesign.bookserp.base.mapper.PublisherMapper;
import org.curriculumdesign.bookserp.base.service.PublisherService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

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
    public Pagination<PublisherDTO> page(Integer pageSize, Integer pageNum, String name, String contactName, String contactMobile) {
        Page<PublisherEntity> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<PublisherEntity> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(PublisherEntity::getName, name);
        }
        if (StringUtils.isNotBlank(contactName)) {
            queryWrapper.like(PublisherEntity::getContactName, contactName);
        }
        if (StringUtils.isNotBlank(contactMobile)) {
            queryWrapper.like(PublisherEntity::getContactMobile, contactMobile);
        }
        IPage<PublisherEntity> bookEntityIPage = mapper.selectPage(page, queryWrapper);
        List<PublisherEntity> records = bookEntityIPage.getRecords();
        List<PublisherDTO> bookDTOS = ListUtils.transferList(records, PublisherDTO.class);
        Pagination<PublisherDTO> result = new Pagination<>(pageNum, pageSize, bookEntityIPage.getTotal(), bookDTOS);
        return result;
    }

    @Override
    public int save(PublisherDTO publisherDTO) {
        if (isExist(publisherDTO.getName(), publisherDTO.getId())) {
            throw new AppException(ResponseCodeEnum.COMMON_EXCEPTION, "出版社名称已存在");
        }
        PublisherEntity publisherEntity = BeanCopyUtils.transferBean(publisherDTO, PublisherEntity.class);
        if (publisherDTO.getId() == null) {
            return mapper.insert(publisherEntity);
        }
        return mapper.updateById(publisherEntity);
    }

    @Override
    public Integer delete(Long id) {
        return mapper.deleteById(id);
    }

    @Override
    public List<PublisherDTO> list() {
        List<PublisherEntity> records = mapper.selectList(Wrappers.emptyWrapper());
        List<PublisherDTO> publisherDTOS = ListUtils.transferList(records, PublisherDTO.class);
        return publisherDTOS;
    }

    @Override
    public PublisherDTO getById(Long id) {
        PublisherEntity entity = mapper.selectById(id);
        return BeanCopyUtils.transferBean(entity, PublisherDTO.class);
    }

    private boolean isExist(String name, Long id) {
        LambdaQueryWrapper<PublisherEntity> queryWrapper = Wrappers.<PublisherEntity>lambdaQuery().eq(PublisherEntity::getName, name);
        if (Objects.nonNull(id)) {
            queryWrapper.ne(PublisherEntity::getId, id);
        }
        return mapper.selectCount(queryWrapper) > 0;
    }
}
