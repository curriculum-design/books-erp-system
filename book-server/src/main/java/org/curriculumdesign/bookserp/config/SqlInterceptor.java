package org.curriculumdesign.bookserp.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})}
)
@Slf4j
public class SqlInterceptor implements Interceptor {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = invocation.proceed();
        } finally {
            try {
                long sqlCostTime = System.currentTimeMillis() - startTime;
                String sql = getSql(configuration, boundSql);
                String returnSql = formatSqlLog(mappedStatement.getSqlCommandType(), sql, sqlCostTime, result);
                mappedStatement.getStatementLog().debug(returnSql);
            } catch (Exception ignored) {
                log.error("Mybatis插件报错");
            }
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        if (properties == null) {
            return;
        }
    }

    private String getSql(Configuration configuration, BoundSql boundSql) {
        // 输入sql字符串空判断
        String sql = boundSql.getSql();
        if (sql == null || sql.length() == 0) {
            return "";
        }

        //美化sql
        sql = beautifySql(sql);

        //填充占位符, 目前基本不用mybatis存储过程调用,故此处不做考虑
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (!parameterMappings.isEmpty() && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = this.replacePlaceholder(sql, parameterObject);
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = replacePlaceholder(sql, obj);
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = replacePlaceholder(sql, obj);
                    }
                }
            }
        }
        return sql;
    }

    private String beautifySql(String sql) {
        return sql.replaceAll("[\\s\n ]+", " ");
    }

    private String replacePlaceholder(String sql, Object parameterObject) {
        String result;
        if (parameterObject instanceof String) {
            result = "'" + parameterObject.toString() + "'";
        } else if (parameterObject instanceof Date) {
            result = "'" + new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(parameterObject) + "'";
        } else if (parameterObject instanceof LocalDateTime) {
            LocalDateTime value = (LocalDateTime) parameterObject;
            result = "'" + value.format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS)) + "'";
        } else if (parameterObject instanceof LocalDate) {
            LocalDate value = (LocalDate) parameterObject;
            result = "'" + value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "'";
        } else {
            result = parameterObject.toString();
        }
        return sql.replaceFirst("\\?", result);
    }

    private String formatSqlLog(SqlCommandType sqlCommandType, String sql, long costTime, Object obj) {
        String returnSql = String.format("[SQL EXECUTE] [%dms] EXPLAIN %s;", costTime, sql);

        if (sqlCommandType == SqlCommandType.UPDATE || sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.DELETE) {
            if (null != obj) {
                returnSql += " rows: ===> " + obj;
            }
        }
        return returnSql;
    }

}