package org.curriculumdesign.bookserp.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 管理员信息
 * </p>
 *
 * @author lesl
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_admin")
public class AdminEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 状态  0：有效；1：无效；
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer status;

    /**
     * 是否删除  0：存在；1：删除；
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(select = false, fill = FieldFill.INSERT)
    private Integer isDel;


}
