package org.curriculumdesign.bookserp.erp.service.impl;

import org.curriculumdesign.bookserp.erp.mapper.OutboundItemMapper;
import org.curriculumdesign.bookserp.erp.service.OutboundItemService;
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
public class OutboundItemServiceImpl implements OutboundItemService {

	@Autowired
    private OutboundItemMapper mapper;
    
}
