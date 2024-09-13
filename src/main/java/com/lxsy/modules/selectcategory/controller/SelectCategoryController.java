package com.lxsy.modules.selectcategory.controller;

import com.lxsy.modules.selectcategory.entity.SelectCategory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @description: 查询种类表
 * @author: husj
 * @create: 2024-07-11
 **/

@RestController
@RequestMapping("/lxsy")
public class SelectCategoryController {


    private Set<Long> getSubtreeIds(Long id, List<SelectCategory> tree) {
        Set<Long> result = new HashSet<>();
        for (SelectCategory node : tree) {
            if (Objects.equals(node.getParentId(), id)) {
                result.add(node.getId());
                // 递归调用获取子节点
                Set<Long> children = getSubtreeIds(node.getId(), tree);
                result.addAll(children);
            }
        }
        return result;
    }
}
