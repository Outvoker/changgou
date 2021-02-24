package com.changgou.search.service;

import java.util.Map;

/**
 * @author Xu Rui
 * @date 2021/2/22 22:48
 */
public interface SkuService {

    /***
     * 条件搜索
     * @param searchMap
     * @return
     */
    Map<String, Object> search(Map<String, String> searchMap);

    /**
     * 导入索引库
     */
    void importSku();
}
