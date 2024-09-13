package com.lxsy.modules.selectcategorycorrelation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 种类菜单和商标等关联关系
 * @author: husj
 * @create: 2024-07-11
 **/
@Data
@TableName(value = "lxsy_select_category_correlation")
public class SelectCategoryCorrelation implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /**
     * 种类菜单id
     */
    private Long categoryId;

    /**
     * 关联的主体id
     */
    private Long correlationId;

    /**
     * 关联的主体类型 1-商标 2-专利权
     */
    private Integer type;
}