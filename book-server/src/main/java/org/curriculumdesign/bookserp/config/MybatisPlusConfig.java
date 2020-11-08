package org.curriculumdesign.bookserp.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.vanrui.spring.boot.starter.context.base.utils.SystemContextUtils;
import com.vanrui.spring.boot.starter.context.constant.IsDelEnum;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @author lesl
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
         paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
         paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }
    /**
     * 自动填充字段值功能
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaHandler();
    }

    public static final class MetaHandler implements MetaObjectHandler {
        private static final String CREATE_TIME_FILED_NAME = "createTime";
        private static final String CREATE_USER_FIELD_NAME = "createUser";
        private static final String UPDATE_TIME_FILED_NAME = "updateTime";
        private static final String UPDATE_USER_FILED_NAME = "updateUser";
        private static final String STATUS_FILED_NAME = "status";
        private static final String IS_DEL_FILED_NAME = "isDel";

        @Override
        public void insertFill(MetaObject metaObject) {
            if (metaObject.hasSetter(CREATE_TIME_FILED_NAME)) {
                this.setInsertFieldValByName(CREATE_TIME_FILED_NAME, LocalDateTime.now(), metaObject);
            }
            if (metaObject.hasSetter(CREATE_USER_FIELD_NAME)) {
                this.setInsertFieldValByName(CREATE_USER_FIELD_NAME, SystemContextUtils.getUserName(), metaObject);
            }
            if (metaObject.hasSetter(UPDATE_TIME_FILED_NAME)) {
                this.setInsertFieldValByName(UPDATE_TIME_FILED_NAME, LocalDateTime.now(), metaObject);
            }
            if (metaObject.hasSetter(UPDATE_USER_FILED_NAME)) {
                this.setInsertFieldValByName(UPDATE_USER_FILED_NAME, SystemContextUtils.getUserName(), metaObject);
            }
            if (metaObject.hasSetter(STATUS_FILED_NAME)) {
                this.setInsertFieldValByName(STATUS_FILED_NAME, IsDelEnum.NORMAL.getCode(), metaObject);
            }
            if (metaObject.hasSetter(IS_DEL_FILED_NAME)) {
                this.setInsertFieldValByName(IS_DEL_FILED_NAME, IsDelEnum.NORMAL.getCode(), metaObject);
            }
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            if (metaObject.hasSetter(UPDATE_TIME_FILED_NAME)) {
                this.setUpdateFieldValByName(UPDATE_TIME_FILED_NAME, LocalDateTime.now(), metaObject);
            }
            if (metaObject.hasSetter(UPDATE_USER_FILED_NAME)) {
                this.setUpdateFieldValByName(UPDATE_USER_FILED_NAME, SystemContextUtils.getUserName(), metaObject);
            }
        }
    }
}
