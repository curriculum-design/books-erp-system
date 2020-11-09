package org.curriculumdesign.bookserp.base.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lesl
 */
@Data
public class SupplierDTO {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 供应商编号
     */
    private String code;

    /**
     * 供应商地址
     */
    private String address;

    /**
     * 联系人手机号
     */
    private String contactMobile;

    /**
     * 联系人姓名
     */
    private String contactName;

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
     * 状态 1:已生效
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 供应商类型
     */
    private String supplierType;

    /**
     * 创建员工ID
     */
    private Long createUser;

    /**
     * 是否删除
     */
    private Boolean isDelete;

}
