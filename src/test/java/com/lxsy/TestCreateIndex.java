package com.lxsy;

import com.lxsy.config.JedisConfig;
import com.lxsy.constant.CommonConstant;
import com.lxsy.utils.JedisSearchUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import redis.clients.jedis.search.Schema;

/**
 * @description: create index
 * @author: husj
 * @create: 2024-09-13
 **/
@SpringBootTest(classes = MyRedisStackApplication.class)
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class TestCreateIndex {

    private final Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    @Autowired
    private JedisSearchUtil jedisSearchUtil;

    @Test
    public void createIndex() {
        logger.info("create index start.............................................");
        Schema schema = new Schema()
                .addField(new Schema.TextField("name"))
                .addField(new Schema.TextField("keyword"))
                .addField(new Schema.TextField("category"));
        jedisSearchUtil.createIndex(CommonConstant.KEYWORDS_INDEX_NAME, "intellectualProperty", schema);
        logger.info("create index end...................................");
    }
}
