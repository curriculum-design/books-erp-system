package org.curriculumdesign.bookserp.base.service;

import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.dto.StorageDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
public interface StorageService {

    Pagination<StorageDTO> page(Integer pageSize, Integer pageNum, String name);

    int save(StorageDTO publisherDTO);

    Integer delete(Long id);

    List<StorageDTO> list();

    StorageDTO getById(Long id);
}
