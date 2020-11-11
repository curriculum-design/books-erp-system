package org.curriculumdesign.bookserp.base.service;

import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.domain.BookEntity;
import org.curriculumdesign.bookserp.base.dto.BookDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
public interface BookService {

    Pagination<BookDTO> page(Integer pageSize, Integer pageNum);
}
