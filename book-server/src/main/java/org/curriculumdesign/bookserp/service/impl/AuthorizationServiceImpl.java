package org.curriculumdesign.bookserp.service.impl;

import com.vanrui.spring.boot.starter.security.service.AuthorizationService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author lesl
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    @Override
    public Collection<String> listRoles(Object userId) {
        return null;
    }

    @Override
    public Collection<String> listPermissions(Object userId) {
        return null;
    }
}
