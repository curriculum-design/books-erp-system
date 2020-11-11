package org.curriculumdesign.bookserp.base.service;

import org.cdteam.spring.cloud.starter.context.bean.Pagination;
import org.curriculumdesign.bookserp.base.dto.ClientDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
public interface ClientService {

    Pagination<ClientDTO> page(Integer pageSize, Integer pageNum, String name, String contactName, String contactMobile);

    Integer save(ClientDTO clientDTO);

    Integer delete(Long id);

    ClientDTO getById(Long id);

    List<ClientDTO> list();
}
