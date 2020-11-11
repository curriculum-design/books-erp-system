package org.curriculumdesign.bookserp.sys.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminDTO {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 状态  0：有效；1：无效；
     */
    private Integer status;

    /**
     * 是否删除  0：存在；1：删除；
     */
    @TableLogic
    private Integer isDel;
}
