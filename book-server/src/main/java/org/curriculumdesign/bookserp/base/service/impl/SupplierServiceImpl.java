package org.curriculumdesign.bookserp.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.cdteam.spring.cloud.starter.common.utils.ListUtils;
import org.curriculumdesign.bookserp.base.domain.SupplierEntity;
import org.curriculumdesign.bookserp.base.dto.SupplierDTO;
import org.curriculumdesign.bookserp.base.mapper.SupplierMapper;
import org.curriculumdesign.bookserp.base.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 客户附件表 服务实现类
 * </p>
 *
 * @author lesl
 * @since 2020-11-09
 */
@Service
public class SupplierServiceImpl implements SupplierService {

	@Autowired
    private SupplierMapper mapper;

    @Override
    public List<SupplierDTO> findAll() {
        List<SupplierEntity> supplierEntities = mapper.selectList(Wrappers.emptyWrapper());
        return ListUtils.transferList(supplierEntities, SupplierDTO.class);
    }
}
