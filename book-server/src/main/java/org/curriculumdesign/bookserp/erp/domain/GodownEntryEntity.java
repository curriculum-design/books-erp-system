package org.curriculumdesign.bookserp.erp.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lesl
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("erp_godown_entry")
public class GodownEntryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 进货时间
     */
    private LocalDateTime operateTime;

    /**
     * 状态
     */
    private Integer status;


}
