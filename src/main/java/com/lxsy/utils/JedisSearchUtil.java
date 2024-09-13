package com.lxsy.utils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
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
import redis.clients.jedis.search.IndexDefinition;
import redis.clients.jedis.search.IndexOptions;
import redis.clients.jedis.search.Schema;

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

    public boolean addBrandIndex(String keyPrefix, BrandVO brandVO) {
        addBrand(keyPrefix, brandVO);
        return true;
    }

    private void addBrand(String keyPrefix, BrandVO brandVO) {
        BrandDTO target = new BrandDTO();
        MyBeanUtil.copyProperties(brandVO, target);
        Map<String, String> hash = MyBeanUtil.toMap(target);
        hash.put("_language", CommonConstant.REDIS_INDEX_LANGUAGE);
        client.hset(keyPrefix + brandVO.getId(), hash);
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
}
