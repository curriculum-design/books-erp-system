package org.curriculumdesign.bookserp.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.curriculumdesign.bookserp.common.model.ColumnEntity;
import org.curriculumdesign.bookserp.sys.domain.AdminEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonMapper {
    @Select("SELECT DISTINCT column_name,column_comment FROM INFORMATION_SCHEMA.Columns" +
            " WHERE table_name=#{tableName} and table_schema=#{tableSchema}")
    List<ColumnEntity> listColumn(String tableName, String tableSchema);

    @Select("SELECT k.column_name FROM information_schema.table_constraints t" +
            " JOIN information_schema.key_column_usage k" +
            " USING (constraint_name,table_schema,table_name)" +
            " WHERE t.constraint_type='PRIMARY KEY'" +
            " AND t.table_schema=#{tableSchema}" +
            " AND t.table_name=#{tableName}")
    String getPrimaryKey(String tableName, String tableSchema);
}