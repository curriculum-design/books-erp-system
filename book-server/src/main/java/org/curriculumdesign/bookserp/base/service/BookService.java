package org.curriculumdesign.bookserp.base.service;

import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.dto.BookDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
public interface BookService {

    Pagination<BookDTO> page(Integer pageSize, Integer pageNum, Long typeId, Long publisherId, String code, String name);

    Integer save(BookDTO bookDTO);

    Integer delete(Long id);

    List<BookDTO> list();

    BookDTO getById(Long id);
}
