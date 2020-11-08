package org.curriculumdesign.bookserp.controller;

import org.curriculumdesign.bookserp.dto.LoginDTO;
import com.vanrui.spring.boot.starter.context.base.RequestContext;
import com.vanrui.spring.boot.starter.context.bean.R;
import com.vanrui.spring.boot.starter.security.annotation.AnonymousAccess;
import com.vanrui.spring.boot.starter.security.token.TokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lesl
 */
@Slf4j
@Api(value = "用户登录管理", tags = "[系统模块]用户登录管理")
@RestController
@RequestMapping
public class LoginController extends BaseController {

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TokenProvider tokenProvider;

    @ApiOperation("登录")
    @PostMapping(value = "/login")
    @AnonymousAccess
    public R login(@RequestBody LoginDTO userLogin) {
        if (userLogin == null || StringUtils.isEmpty(userLogin.getUsername())
                || StringUtils.isEmpty(userLogin.getPassword())) {
            return R.error("用户名和密码不能为空!");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        log.info("登录后获取到的===token:{}", token);
        if (StringUtils.isEmpty(token)) {
            return R.error("登录失败!");
        } else {
            Map<String, String> tokenMap = new HashMap<>(1);
            tokenMap.put("token", token);
            return R.data(tokenMap);
        }
    }

    @ApiOperation("上下文")
    @GetMapping(value = "/login-context")
    public R requestContext() {
        RequestContext userContext = getUserContext();
        return R.data(userContext.toStream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @ApiOperation("获取联系人列表")
    @GetMapping(value = "/contact-list")
    @AnonymousAccess
    public R contactList() {
        log.info("获取联系人列表, 租户: {}", getTenantId());
        String response = restTemplate.getForObject("http://localhost:18201/contact/list", String.class);
        log.info("获取联系人完成, 租户: {}, {}", getTenantId(), response);
        return R.ok();
    }
}
