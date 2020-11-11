package org.curriculumdesign.bookserp.base.service;

import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.dto.BookTypeDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
public interface BookTypeService {

    Pagination<BookTypeDTO> page(Integer pageSize, Integer pageNum, String name);

    int save(BookTypeDTO publisherDTO);

    Integer delete(Long id);

    List<BookTypeDTO> list();

    BookTypeDTO getById(Long id);
}
