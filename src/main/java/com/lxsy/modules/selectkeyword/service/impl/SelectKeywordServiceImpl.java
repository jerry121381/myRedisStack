package com.lxsy.modules.selectkeyword.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxsy.common.exception.ServiceException;
import com.lxsy.constant.ResultCode;
import com.lxsy.modules.selectkeyword.condition.SelectKeywordCondition;
import com.lxsy.modules.selectkeyword.dto.SelectKeywordDTO;
import com.lxsy.modules.selectkeyword.entity.SelectKeyword;
import com.lxsy.modules.selectkeyword.mapper.SelectKeywordMapper;
import com.lxsy.modules.selectkeyword.service.ISelectKeywordService;
import com.lxsy.modules.selectkeyword.vo.SelectKeywordVO;
import org.springblade.lxsycore.secure.utils.SecureUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 查询关键字
 * @author: husj
 * @create: 2024-07-11
 **/
@Service
public class SelectKeywordServiceImpl extends ServiceImpl<SelectKeywordMapper, SelectKeyword> implements ISelectKeywordService {
}




