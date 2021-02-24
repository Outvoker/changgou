package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Xu Rui
 * @date 2021/2/22 22:51
 */
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo,Long> {
}
