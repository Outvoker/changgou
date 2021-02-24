package com.changgou.item.controller;

import com.changgou.item.service.PageService;
import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述
 *
 * @author Xu Rui
 * @version 1.0
 * @package com.changgou.item.controller *
 * @since 1.0
 */

@RestController
@RequestMapping("/page")
public class PageController {
    @Resource
    private PageService pageService;

    /**
     * 生成静态页面
     * @param id SPU的ID
     * @return
     */
    @RequestMapping("/createHtml/{id}")
    public Result createHtml(@PathVariable(name="id") Long id){
        pageService.createPageHtml(id);
        return new Result(true, StatusCode.OK,"ok");
    }
}
