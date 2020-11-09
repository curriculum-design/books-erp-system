package org.curriculumdesign.bookserp.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.curriculumdesign.bookserp.domain.AdminEntity;
import org.curriculumdesign.bookserp.dto.UserAccountDTO;
import org.curriculumdesign.bookserp.mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service("adminAuthServiceImpl")
public class AdminAuthServiceImpl implements UserDetailsService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminEntity admin = adminMapper.selectOne(Wrappers.<AdminEntity>lambdaQuery().eq(AdminEntity::getUsername, username));
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername(admin.getUsername());
        userAccountDTO.setId(admin.getId());
        userAccountDTO.setStatus(admin.getStatus());
        userAccountDTO.setPassword(admin.getPassword());
        userAccountDTO.setCredentialsSalt(admin.getUsername());
        return userAccountDTO;
    }

}
