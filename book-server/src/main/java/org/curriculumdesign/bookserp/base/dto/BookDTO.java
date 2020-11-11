package org.curriculumdesign.bookserp.base.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookDTO {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建员工
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean isDel;

    /**
     * 图书编号
     */
    private String code;

    /**
     * 图书名称
     */
    private String name;

    /**
     * 图书类型ID
     */
    private Long typeId;

    /**
     * 出版社ID
     */
    private Long publisherId;

}
