package org.curriculumdesign.bookserp.base.service;

import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.dto.PublisherDTO;

import java.util.List;

/**
 * <p>
 * 出版社 服务类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
public interface PublisherService {

    Pagination<PublisherDTO> page(Integer pageSize, Integer pageNum, String name, String contactName, String contactMobile);

    int save(PublisherDTO publisherDTO);

    Integer delete(Long id);

    List<PublisherDTO> list();

    PublisherDTO getById(Long id);
}
