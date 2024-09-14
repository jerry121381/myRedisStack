package com.lxsy.modules.brand.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: brand VO
 * @author: husj
 * @create: 2024-09-13
 **/
@Data
public class BrandCacheVO implements Serializable {
    private static final long serialVersionUID = 6637505084948390239L;

    /**
     * 组织-商标编号
     */
    private String id;
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

    private List<String> categoryIds;
}
