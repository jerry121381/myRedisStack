package com.lxsy.modules.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: brand DTO
 * @author: husj
 * @create: 2024-09-13
 **/
@Data
public class BrandDTO implements Serializable {
    private static final long serialVersionUID = 7291308230178185804L;
    /**
     * 组织-商标编号
     */
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
     * 用户举报时查询是否显示 0:不显示 1:显示
     */
    private Integer isShow;

    private List<String> keywords;

    private List<Long> categoryIds;

}
