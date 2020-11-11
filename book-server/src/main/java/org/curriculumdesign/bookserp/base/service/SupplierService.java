package org.curriculumdesign.bookserp.base.service;

import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.dto.SupplierDTO;

import java.util.List;

/**
 * <p>
 * 客户附件表 服务类
 * </p>
 *
 * @author lesl
 * @since 2020-11-09
 */
public interface SupplierService {

    Pagination<SupplierDTO> page(Integer pageSize, Integer pageNum, String name, String code, String contactName, String contactMobile);

    Integer save(SupplierDTO supplierDTO);

    Integer delete(Long id);

    List<SupplierDTO> list();

    SupplierDTO getById(Long id);
}
