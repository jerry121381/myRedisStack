package com.lxsy.modules.selectkeywordcorrelation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: api举报时查询关键字与知识产权的关联
 * @author: husj
 * @create: 2024-07-12
 **/
@TableName(value = "lxsy_select_keyword_correlation")
@Data
public class SelectKeywordCorrelation implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关键字id
     */
    private Long keywordId;

    /**
     * 关联的主体id
     */
    private Long correlationId;

    /**
     * 关联的主体类型 1-商标 2-专利权
     */
    private Integer type;
}