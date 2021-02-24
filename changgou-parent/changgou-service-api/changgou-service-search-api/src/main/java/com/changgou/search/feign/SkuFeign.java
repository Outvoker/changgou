package com.changgou.search.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Xu Rui
 * @date 2021/2/23 17:36
 */
@FeignClient(name = "search")
@RequestMapping(value = "/search")
public interface SkuFeign {
    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping
    Result<Map<String, Object>> search(@RequestParam(required = false) Map<String, String> searchMap);
}
