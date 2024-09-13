package com.lxsy.modules.brand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description: brand
 * @author: husj
 * @create: 2024-09-13
 **/
@Data
@TableName("lxsy_brand")
public class Brand implements Serializable {
    private static final long serialVersionUID = 8439987616336944784L;

    /**
     * 组织-商标编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 商标号
     */
    private String brandNumber;
    /**
     * 商标名称
     */
    private String brandName;
    /**
     * 商标证明
     */
    private String brandImage;
    /**
     * 商标证明
     */
    private String brandProveImage;
    /**
     * 联合声明
     */
    private String jointDeclaration;
    /**
     * 商标状态,0:未审核, 1:已审核
     */
    private Integer brandStatus;
    /**
     * 删除标识：0-正常，1-已删除
     */
    @TableLogic
    private Integer isDeleted;
    /**
     * 租户CODE
     */
    private String tenantCode;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间(@TableField(update = "NOW()")自动设置修改时间为当前数据库时间)
     */
    private LocalDateTime updateTime;
    /**
     * 用户举报时查询是否显示 0:不显示 1:显示
     */
    private Integer isShow;
}
