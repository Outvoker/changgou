package com.changgou.search.controller;

import com.changgou.search.service.SkuService;
import entity.Result;
import entity.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Xu Rui
 * @date 2021/2/22 22:59
 */
@RestController
@RequestMapping(value = "/search")
@CrossOrigin
@Slf4j
public class SkuController {

    @Resource
    private SkuService skuService;

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping
    public Result<Map<String, Object>> search(@RequestParam(required = false) Map<String, String> searchMap){
        log.info("search({})", searchMap);
        return  new Result<>(true, StatusCode.OK, "查询成功！", skuService.search(searchMap));
    }

    /**
     * 导入数据
     * @return
     */
    @GetMapping("/import")
    public Result importSku(){
        log.info("importSku()");
        skuService.importSku();
        return new Result(true, StatusCode.OK,"导入数据到索引库中成功！");
    }
}