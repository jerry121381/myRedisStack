package com.lxsy;

import com.lxsy.config.JedisConfig;
import com.lxsy.constant.CommonConstant;
import com.lxsy.modules.brand.vo.BrandCacheVO;
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
import redis.clients.jedis.search.SearchResult;

import java.util.Arrays;
import java.util.Set;

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
                .addField(new Schema.TextField("brandName"))
                .addField(new Schema.TextField("keywords"))
                .addField(new Schema.TextField("categoryIds"));
        jedisSearchUtil.createIndex(CommonConstant.KEYWORDS_INDEX_NAME, "search:intellectual:", schema);
        logger.info("create index end...................................");
    }

    @Test
    public void indexList() {
        Set<String> strings = jedisSearchUtil.indexList();
        logger.info("所有的索引{}", strings);
    }

    @Test
    public void addBrandIndex() {
        BrandCacheVO brandCacheVO = new BrandCacheVO();
        // brandVO.setId(100001);
        // brandVO.setBrandNumber("123456789");
        // brandVO.setBrandName("测试商标1");
        // brandVO.setBrandImage("https://www.baidu.com");
        // brandVO.setBrandProveImage("https://www.baidu.com");
        // brandVO.setJointDeclaration("xxxxxxxxxxxxxxxxxx");
        // brandVO.setIsShow(1);
        // brandVO.setKeywords(Arrays.asList("詹品", "小凤仙"));
        // brandVO.setCategoryIds(Arrays.asList(888888L, 8888889L));
        // jedisSearchUtil.addBrandIndex(CommonConstant.INTELLECTUAL_PROPERTY_PACKAGE, brandVO);
        // brandVO.setId(100002);
        // brandVO.setBrandNumber("123456789");
        // brandVO.setBrandName("乳山牡蛎");
        // brandVO.setBrandImage("https://www.baidu.com");
        // brandVO.setBrandProveImage("https://www.baidu.com");
        // brandVO.setJointDeclaration("xxxxxxxxxxxxxxxxxx");
        // brandVO.setIsShow(1);
        // brandVO.setKeywords(Arrays.asList("海鲜", "水产"));
        // brandVO.setCategoryIds(Collections.singletonList(8888889L));
        // jedisSearchUtil.addBrandIndex(CommonConstant.INTELLECTUAL_PROPERTY_PACKAGE, brandVO);
        brandCacheVO.setId("100003");
        brandCacheVO.setBrandNumber("123456789");
        brandCacheVO.setBrandName("绿水韵味");
        brandCacheVO.setBrandImage("https://www.baidu.com");
        brandCacheVO.setBrandProveImage("https://www.baidu.com");
        brandCacheVO.setJointDeclaration("xxxxxxxxxxxxxxxxxx");
        brandCacheVO.setIsShow(1);
        brandCacheVO.setKeywords(Arrays.asList("水果", "烟台苹果"));
        brandCacheVO.setCategoryIds(Arrays.asList("8888889", "8888881"));
        boolean b = jedisSearchUtil.addBrandIndex(CommonConstant.INTELLECTUAL_PROPERTY_PACKAGE, brandCacheVO);
    }

    @Test
    public void queryByCondition() {
        String keyword = "詹品";
        Long categoryId = 888L;
        SearchResult searchResult = jedisSearchUtil.queryBrand(CommonConstant.KEYWORDS_INDEX_NAME, keyword, categoryId);
        logger.info("查询结果{}", searchResult);
    }

    @Test
    public void createJsonIndex() {
        logger.info("create index start.............................................");
        Schema schema = new Schema()
                .addTextField("$.brandName", 1).as("brandName")
                .addTagField("$.keywords[*]").as("keywords")
                .addTagField("$.categoryIds[*]").as("categoryIds");
        jedisSearchUtil.createJsonIndex(CommonConstant.INTELLECTUAL_PROPERTY_JSON_INDEX_NAME, CommonConstant.INTELLECTUAL_PROPERTY_JSON_INDEX_PREFIX, schema);
        logger.info("create index end...................................");
    }

    @Test
    public void addJsonBrand() {
        BrandCacheVO brandCacheVO = new BrandCacheVO();
        // brandCacheVO.setId("100001");
        // brandCacheVO.setBrandNumber("123456789");
        // brandCacheVO.setBrandName("测试商标1");
        // brandCacheVO.setBrandImage("https://www.baidu.com");
        // brandCacheVO.setBrandProveImage("https://www.baidu.com");
        // brandCacheVO.setJointDeclaration("xxxxxxxxxxxxxxxxxx");
        // brandCacheVO.setIsShow(1);
        // brandCacheVO.setKeywords(Arrays.asList("詹品", "小凤仙"));
        // brandCacheVO.setCategoryIds(Arrays.asList("888888", "8888889"));
        // jedisSearchUtil.addJsonBrand(CommonConstant.INTELLECTUAL_PROPERTY_JSON_INDEX_PREFIX, brandCacheVO);

        // brandCacheVO.setId("100002");
        // brandCacheVO.setBrandNumber("123456789");
        // brandCacheVO.setBrandName("乳山牡蛎");
        // brandCacheVO.setBrandImage("https://www.baidu.com");
        // brandCacheVO.setBrandProveImage("https://www.baidu.com");
        // brandCacheVO.setJointDeclaration("xxxxxxxxxxxxxxxxxx");
        // brandCacheVO.setIsShow(1);
        // brandCacheVO.setKeywords(Arrays.asList("海鲜", "水产"));
        // brandCacheVO.setCategoryIds(Collections.singletonList("8888889"));
        // jedisSearchUtil.addJsonBrand(CommonConstant.INTELLECTUAL_PROPERTY_JSON_INDEX_PREFIX, brandCacheVO);

        brandCacheVO.setId("100003");
        brandCacheVO.setBrandNumber("123456789");
        brandCacheVO.setBrandName("绿水韵味");
        brandCacheVO.setBrandImage("https://www.baidu.com");
        brandCacheVO.setBrandProveImage("https://www.baidu.com");
        brandCacheVO.setJointDeclaration("xxxxxxxxxxxxxxxxxx");
        brandCacheVO.setIsShow(1);
        brandCacheVO.setKeywords(Arrays.asList("水果", "烟台苹果"));
        brandCacheVO.setCategoryIds(Arrays.asList("8888889", "8888881"));
        boolean b = jedisSearchUtil.addJsonBrand(CommonConstant.INTELLECTUAL_PROPERTY_JSON_INDEX_PREFIX, brandCacheVO);
    }

    @Test
    public void queryJsonByCondition() {
        // String keyword = "乳山牡蛎";
        String keyword = "";
        Long categoryId = 8888889L;
        SearchResult searchResult = jedisSearchUtil.queryJsonBrand(CommonConstant.INTELLECTUAL_PROPERTY_JSON_INDEX_NAME, keyword, categoryId);
        logger.info("查询结果{}", searchResult);
    }
}
