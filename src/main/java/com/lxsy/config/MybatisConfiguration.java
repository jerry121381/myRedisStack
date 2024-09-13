package com.lxsy.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description: mybatis config
 * @author: husj
 * @create: 2024-09-13
 **/
@Configuration
@MapperScan("com.lxsy.**.mapper")
public class MybatisConfiguration {
}
