package com.lxsy.modules.selectcategory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description: 查询种类表
 * @author: husj
 * @create: 2024-07-11
 **/
@Data
@TableName(value = "lxsy_select_category")
public class SelectCategory implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 种类名
     */
    private String name;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态 1-正常 2-禁用
     */
    private Integer status;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人id
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标识
     */
    @TableLogic
    private Integer isDeleted;
}