package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xu Rui
 * @date 2021/2/22 22:44
 */
@FeignClient(value="goods")
@RequestMapping("/sku")
public interface SkuFeign {

    /***
     * 库存递减
     * @param username
     * @return
     */
    @PostMapping(value = "/decr/count")
    Result decrCount(@RequestParam(value = "username") String username);

    /***
     * 根据ID查询SKU信息
     * @param id : sku的ID
     */
    @GetMapping(value = "/{id}")
    Result<Sku> findById(@PathVariable(value = "id", required = true) Long id);

    /***
     * 根据审核状态查询Sku
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable String status);

    /**
     * 根据条件搜索
     * @param sku
     * @return
     */
    @PostMapping(value = "/search" )
    Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);

}
