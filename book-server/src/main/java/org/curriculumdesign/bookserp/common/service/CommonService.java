package org.curriculumdesign.bookserp.common.service;

import org.curriculumdesign.bookserp.common.mapper.CommonMapper;
import org.curriculumdesign.bookserp.common.model.ColumnEntity;
import org.curriculumdesign.bookserp.common.model.TableInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Diamond on 2017/7/19.
 */
@Service
public class CommonService {

    @Autowired
    CommonMapper commonMapper;

    public TableInfoEntity selectTableFields(String table) {

        String schemaName = "books";
        List<ColumnEntity> columnEntities = commonMapper.listColumn(table, schemaName);
        Map<String, String> nameCommentMapping = new LinkedHashMap<>();
        columnEntities.forEach(columnEntity -> {
            String name = toCamelCase(columnEntity.getColumnName());
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            String comment = columnEntity.getColumnComment();
            nameCommentMapping.put(name, comment);
        });
        TableInfoEntity entity = new TableInfoEntity();
        entity.setFields(nameCommentMapping);
        entity.setTableName(table);

        String primaryKey = commonMapper.getPrimaryKey(table, schemaName);
        entity.setPrimaryKey(primaryKey);
        return entity;
    }
    /**
     * Convert a string to camel case
     */
    public static String toCamelCase(String string) {
        StringBuilder result = new StringBuilder();

        // [#2515] - Keep trailing underscores
        for (String word : string.split("_", -1)) {

            // Uppercase first letter of a word
            if (word.length() > 0) {

                // [#82] - If a word starts with a digit, prevail the
                // underscore to prevent naming clashes
                if (Character.isDigit(word.charAt(0))) {
                    result.append("_");
                }

                result.append(word.substring(0, 1).toUpperCase());
                result.append(word.substring(1).toLowerCase());
            }

            // If no letter exists, prevail the underscore (e.g. leading
            // underscores)
            else {
                result.append("_");
            }
        }

        return result.toString();
    }
}
