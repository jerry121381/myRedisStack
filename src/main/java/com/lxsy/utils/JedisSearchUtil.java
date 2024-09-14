package com.lxsy.utils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxsy.constant.CommonConstant;
import com.lxsy.modules.brand.dto.BrandDTO;
import com.lxsy.modules.brand.vo.BrandVO;
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
     * @param brandVO 商标信息
     * @return 是否成功
     */
    public boolean addBrandIndex(String keyPrefix, BrandVO brandVO) {
        BrandDTO target = new BrandDTO();
        MyBeanUtil.copyProperties(brandVO, target);
        Map<String, String> hash = MyBeanUtil.toMap(target);
        hash.put("_language", CommonConstant.REDIS_INDEX_LANGUAGE);
        client.hset(keyPrefix + brandVO.getId(), hash);
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

    private void addBrand(String keyPrefix, BrandVO brandVO) {

    }


    public boolean addBrandList(String keyPrefix, List<BrandVO> brandVOList) {
        int chunk = 100;
        List<List<BrandVO>> partition = ListUtil.partition(brandVOList, chunk); // 将list 切割为指定大小的子list
        AbstractPipeline pipelined = client.pipelined();
        for (List<BrandVO> brands : partition) {
            for (BrandVO brand : brands) {
                BrandDTO brandDTO = new BrandDTO();
                BeanUtils.copyProperties(brand, brandDTO);
                Map<String, String> hash = MyBeanUtil.toMap(brandDTO);
                String pinyin = PinyinUtil.getPinyin(brandDTO.getBrandName(), "");
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
     * @param brandVO 商标信息
     * @return 是否成功
     */
    public boolean addJsonBrand(String keyPrefix, BrandVO brandVO) {
        BrandDTO brandDTO = new BrandDTO();
        BeanUtils.copyProperties(brandVO, brandDTO);
        try {
            String s = objectMapper.writeValueAsString(brandDTO);
            client.jsonSet(keyPrefix + brandVO.getId(), s);
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
