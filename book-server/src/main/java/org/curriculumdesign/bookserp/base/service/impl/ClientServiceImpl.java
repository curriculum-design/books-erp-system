package org.curriculumdesign.bookserp.base.service.impl;

import org.curriculumdesign.bookserp.base.mapper.ClientMapper;
import org.curriculumdesign.bookserp.base.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
    private ClientMapper mapper;
    
}
