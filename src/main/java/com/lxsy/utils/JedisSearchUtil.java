package com.lxsy.utils;

import com.lxsy.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.IndexDefinition;
import redis.clients.jedis.search.IndexOptions;
import redis.clients.jedis.search.Schema;

import java.util.Set;

/**
 * @description: redisearch util
 * @author: husj
 * @create: 2024-09-13
 **/
@Component
public class JedisSearchUtil {
    private final Logger logger = LoggerFactory.getLogger(JedisSearchUtil.class);

    @Autowired
    private UnifiedJedis client;

    /**
     * 创建索引
     * @param idxName 索引名称
     * @param prefix  要索引的数据前缀
     * @param schema  索引字段配置
     */
    public void createIndex(String idxName, String prefix, Schema schema) {
        IndexDefinition rule = new IndexDefinition(IndexDefinition.Type.HASH)
                .setPrefixes(prefix)
                .setLanguage(CommonConstant.REDIS_INDEX_LANGUAGE);
        client.ftCreate(idxName, IndexOptions.defaultOptions().setDefinition(rule), schema);
    }

    public Set<String> indexList() {
        return client.ftList();
    }
}
