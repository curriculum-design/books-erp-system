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
import org.curriculumdesign.bookserp.base.domain.SupplierEntity;
import org.curriculumdesign.bookserp.base.domain.BookTypeEntity;
import org.curriculumdesign.bookserp.base.domain.SupplierEntity;
import org.curriculumdesign.bookserp.base.dto.SupplierDTO;
import org.curriculumdesign.bookserp.base.mapper.SupplierMapper;
import org.curriculumdesign.bookserp.base.service.SupplierService;
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
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper mapper;

    @Override
    public Pagination<SupplierDTO> page(Integer pageSize, Integer pageNum, String name, String Code, String contactName, String contactMobile) {
        Page<SupplierEntity> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<SupplierEntity> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(SupplierEntity::getName, name);
        }
        if (StringUtils.isNotBlank(Code)) {
            queryWrapper.like(SupplierEntity::getCode, Code);
        }
        if (StringUtils.isNotBlank(contactName)) {
            queryWrapper.like(SupplierEntity::getContactName, contactName);
        }
        if (StringUtils.isNotBlank(contactMobile)) {
            queryWrapper.like(SupplierEntity::getContactMobile, contactMobile);
        }
        IPage<SupplierEntity> bookEntityIPage = mapper.selectPage(page, queryWrapper);
        List<SupplierEntity> records = bookEntityIPage.getRecords();
        List<SupplierDTO> supplierDTOS = ListUtils.transferList(records, SupplierDTO.class);
        Pagination<SupplierDTO> result = new Pagination<>(pageNum, pageSize, bookEntityIPage.getTotal(), supplierDTOS);
        return result;
    }

    @Override
    public Integer save(SupplierDTO supplierDTO) {
        if (isExistByName(supplierDTO.getName(), supplierDTO.getId())) {
            throw new AppException(ResponseCodeEnum.COMMON_EXCEPTION, "名称已存在");
        }
        if (isExistByCode(supplierDTO.getCode(), supplierDTO.getId())) {
            throw new AppException(ResponseCodeEnum.COMMON_EXCEPTION, "编号已存在");
        }
        SupplierEntity supplierEntity = BeanCopyUtils.transferBean(supplierDTO, SupplierEntity.class);
        if (supplierDTO.getId() == null) {
            return mapper.insert(supplierEntity);
        }
        return mapper.updateById(supplierEntity);
    }

    @Override
    public Integer delete(Long id) {
        return mapper.deleteById(id);
    }

    @Override
    public List<SupplierDTO> list() {
        List<SupplierEntity> records = mapper.selectList(Wrappers.emptyWrapper());
        List<SupplierDTO> supplierDTOS = ListUtils.transferList(records, SupplierDTO.class);
        return supplierDTOS;
    }

    @Override
    public SupplierDTO getById(Long id) {
        SupplierEntity entity = mapper.selectById(id);
        return BeanCopyUtils.transferBean(entity, SupplierDTO.class);
    }

    private boolean isExist(String name, Long id) {
        LambdaQueryWrapper<SupplierEntity> queryWrapper = Wrappers.<SupplierEntity>lambdaQuery().eq(SupplierEntity::getName, name);
        if (Objects.nonNull(id)) {
            queryWrapper.ne(SupplierEntity::getId, id);
        }
        return mapper.selectCount(queryWrapper) > 0;
    }
    private boolean isExistByName(String name, Long id) {
        LambdaQueryWrapper<SupplierEntity> queryWrapper = Wrappers.<SupplierEntity>lambdaQuery().eq(SupplierEntity::getName, name);
        if (Objects.nonNull(id)) {
            queryWrapper.ne(SupplierEntity::getId, id);
        }
        return mapper.selectCount(queryWrapper) > 0;
    }
    private boolean isExistByCode(String code, Long id) {
        LambdaQueryWrapper<SupplierEntity> queryWrapper = Wrappers.<SupplierEntity>lambdaQuery().eq(SupplierEntity::getCode, code);
        if (Objects.nonNull(id)) {
            queryWrapper.ne(SupplierEntity::getId, id);
        }
        return mapper.selectCount(queryWrapper) > 0;
    }
}
