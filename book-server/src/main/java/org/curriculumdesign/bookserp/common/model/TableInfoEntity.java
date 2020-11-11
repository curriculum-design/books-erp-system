package org.curriculumdesign.bookserp.common.model;

import java.util.Map;

/**
 * Created by Diamond on 2017/7/19.
 */
public class TableInfoEntity {
    private String tableName;
    private String primaryKey;
    private Map<String, String> fields;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
