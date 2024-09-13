package com.lxsy.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;

/**
 * @description: Jedis config
 * @author: husj
 * @create: 2024-09-13
 **/
@Configuration
public class JedisConfig {

    private final Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWaitMillis;


    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setJmxEnabled(false);
        return jedisPoolConfig;
    }

    @Bean
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {
        JedisPool jedisPool;
        if (StringUtils.isNotEmpty(password)) {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
        } else {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, null, database);
        }
        return jedisPool;
    }

    @Bean
    public UnifiedJedis unifiedJedis(GenericObjectPoolConfig jedisPoolConfig) {
        UnifiedJedis client;
        if (StringUtils.isNotEmpty(password)) {
            client = new JedisPooled(jedisPoolConfig, host, port, timeout, password, database);
        } else {
            client = new JedisPooled(jedisPoolConfig, host, port, timeout, null, database);
        }
        return client;
    }
}