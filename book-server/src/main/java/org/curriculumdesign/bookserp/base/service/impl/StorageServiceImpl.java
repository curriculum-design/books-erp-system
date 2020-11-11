package org.curriculumdesign.bookserp.base.service.impl;

import org.curriculumdesign.bookserp.base.mapper.StorageMapper;
import org.curriculumdesign.bookserp.base.service.StorageService;
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
public class StorageServiceImpl implements StorageService {

	@Autowired
    private StorageMapper mapper;
    
}
