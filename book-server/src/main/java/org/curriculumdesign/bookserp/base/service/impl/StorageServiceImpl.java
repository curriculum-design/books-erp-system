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
import org.curriculumdesign.bookserp.base.domain.StorageEntity;
import org.curriculumdesign.bookserp.base.dto.StorageDTO;
import org.curriculumdesign.bookserp.base.mapper.StorageMapper;
import org.curriculumdesign.bookserp.base.service.StorageService;
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
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageMapper mapper;

    @Override
    public Pagination<StorageDTO> page(Integer pageSize, Integer pageNum, String name) {
        Page<StorageEntity> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<StorageEntity> queryWrapper = Wrappers.<StorageEntity>lambdaQuery();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(StorageEntity::getName, name);
        }
        IPage<StorageEntity> bookEntityIPage = mapper.selectPage(page, queryWrapper);
        List<StorageEntity> records = bookEntityIPage.getRecords();
        List<StorageDTO> bookDTOS = ListUtils.transferList(records, StorageDTO.class);
        Pagination<StorageDTO> result = new Pagination<>(pageNum, pageSize, bookEntityIPage.getTotal(), bookDTOS);
        return result;
    }


    @Override
    public int save(StorageDTO publisherDTO) {
        if (isExist(publisherDTO.getName(), publisherDTO.getId())) {
            throw new AppException(ResponseCodeEnum.COMMON_EXCEPTION, "名称已存在");
        }
        StorageEntity storageEntity = BeanCopyUtils.transferBean(publisherDTO, StorageEntity.class);
        if (publisherDTO.getId() == null) {
            return mapper.insert(storageEntity);
        }
        return mapper.updateById(storageEntity);
    }

    @Override
    public Integer delete(Long id) {
        return mapper.deleteById(id);
    }

    @Override
    public List<StorageDTO> list() {
        List<StorageEntity> records = mapper.selectList(Wrappers.emptyWrapper());
        List<StorageDTO> bookDTOS = ListUtils.transferList(records, StorageDTO.class);
        return bookDTOS;
    }

    @Override
    public StorageDTO getById(Long id) {
        StorageEntity storageEntity = mapper.selectById(id);
        return BeanCopyUtils.transferBean(storageEntity, StorageDTO.class);
    }

    private boolean isExist(String name, Long id) {
        LambdaQueryWrapper<StorageEntity> queryWrapper = Wrappers.<StorageEntity>lambdaQuery().eq(StorageEntity::getName, name);
        if (Objects.nonNull(id)) {
            queryWrapper.ne(StorageEntity::getId, id);
        }
        return mapper.selectCount(queryWrapper) > 0;
    }
}
