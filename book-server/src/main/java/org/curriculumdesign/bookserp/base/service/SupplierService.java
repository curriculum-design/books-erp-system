package org.curriculumdesign.bookserp.base.service;

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

    List<SupplierDTO> findAll();
}
