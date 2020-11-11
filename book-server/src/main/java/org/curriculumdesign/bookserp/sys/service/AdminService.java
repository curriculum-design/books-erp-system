package org.curriculumdesign.bookserp.sys.service;

import org.curriculumdesign.bookserp.sys.dto.AdminDTO;

/**
 * <p>
 * 管理员信息 服务类
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
public interface AdminService {

    AdminDTO getById(Long userId);
}
