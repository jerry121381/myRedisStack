package com.lxsy.utils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxsy.constant.CommonConstant;
import com.lxsy.modules.brand.dto.BrandCacheDTO;
import com.lxsy.modules.brand.vo.BrandCacheVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.AbstractPipeline;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.*;

import java.util.List;
import java.util.Map;
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
    @Autowired
    private ObjectMapper objectMapper;

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

    /**
     * 查询索引列表
     * @return 索引列表
     */
    public Set<String> indexList() {
        return client.ftList();
    }

    /**
     * 添加商标索引
     * @param brandCacheVO 商标信息
     * @return 是否成功
     */
    public boolean addBrandIndex(String keyPrefix, BrandCacheVO brandCacheVO) {
        BrandCacheDTO target = new BrandCacheDTO();
        MyBeanUtil.copyProperties(brandCacheVO, target);
        Map<String, String> hash = MyBeanUtil.toMap(target);
        hash.put("_language", CommonConstant.REDIS_INDEX_LANGUAGE);
        client.hset(keyPrefix + brandCacheVO.getId(), hash);
        return true;
    }

    public SearchResult queryBrand(String indexName, String keyword, Long categoryId) {
        String queryKey = "";
        if (StringUtils.isNotEmpty(keyword)) {
            queryKey += String.format("@brandName:(%s) | @keywords:(%s)", keyword, keyword);
        }
        if (categoryId != null) {
            queryKey += String.format("@categoryIds:(%s)", categoryId);
        }
        Query query = new Query(queryKey);
        query.setLanguage(CommonConstant.REDIS_INDEX_LANGUAGE);
        return client.ftSearch(indexName, query);
    }

    private void addBrand(String keyPrefix, BrandCacheVO brandCacheVO) {

    }


    public boolean addBrandList(String keyPrefix, List<BrandCacheVO> brandCacheVOList) {
        int chunk = 100;
        List<List<BrandCacheVO>> partition = ListUtil.partition(brandCacheVOList, chunk); // 将list 切割为指定大小的子list
        AbstractPipeline pipelined = client.pipelined();
        for (List<BrandCacheVO> brands : partition) {
            for (BrandCacheVO brand : brands) {
                BrandCacheDTO brandCacheDTO = new BrandCacheDTO();
                BeanUtils.copyProperties(brand, brandCacheDTO);
                Map<String, String> hash = MyBeanUtil.toMap(brandCacheDTO);
                String pinyin = PinyinUtil.getPinyin(brandCacheDTO.getBrandName(), "");
                hash.put("pinyin", pinyin);
                hash.put("_language", CommonConstant.REDIS_INDEX_LANGUAGE);
                pipelined.hset(keyPrefix + brand.getId(), hash);
            }
        }
        return true;
    }

    /**
     * 创建json索引
     * @param idxName 索引名称
     * @param prefix  要索引的数据前缀（即索引查询的数据的目标地址）
     * @param schema  索引字段配置
     */
    public void createJsonIndex(String idxName, String prefix, Schema schema) {
        IndexDefinition rule = new IndexDefinition(IndexDefinition.Type.JSON)
                .setPrefixes(prefix)
                .setLanguage(CommonConstant.REDIS_INDEX_LANGUAGE);
        client.ftCreate(idxName, IndexOptions.defaultOptions().setDefinition(rule), schema);
    }

    /**
     * 添加单条商标json索引
     * @param brandCacheVO 商标信息
     * @return 是否成功
     */
    public boolean addJsonBrand(String keyPrefix, BrandCacheVO brandCacheVO) {
        BrandCacheDTO brandCacheDTO = new BrandCacheDTO();
        BeanUtils.copyProperties(brandCacheVO, brandCacheDTO);
        try {
            String s = objectMapper.writeValueAsString(brandCacheDTO);
            client.jsonSet(keyPrefix + brandCacheVO.getId(), s);
        } catch (JsonProcessingException e) {
            logger.error("json序列化失败", e);
        }
        return true;
    }

    public SearchResult queryJsonBrand(String indexName, String keyword, Long categoryId) {
        String queryKey = "";
        if (StringUtils.isNotEmpty(keyword)) {
            queryKey += String.format("@brandName:(%s)", keyword);
        }
        // if (categoryId != null) {
        //     queryKey += String.format("@categoryIds:(%s)", categoryId);
        // }
        Query query = new Query(queryKey);
        query.setLanguage(CommonConstant.REDIS_INDEX_LANGUAGE);
        return client.ftSearch(indexName, query);
    }
}
