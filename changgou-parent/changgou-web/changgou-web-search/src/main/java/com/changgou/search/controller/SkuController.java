package com.changgou.search.controller;

import com.changgou.search.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import entity.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 描述
 *
 * @author Xu Rui
 * @version 1.0
 * @package com.changgou.search.controller *
 * @since 1.0
 */
@Controller
@RequestMapping("/search")
public class SkuController {

    @Resource
    private SkuFeign skuFeign;


    @GetMapping("/list")
    public String search(@RequestParam(required = false) Map<String, String> searchMap, Model model) {
        //1.调用搜索微服务的 feign  根据搜索的条件参数 查询 数据
        Map<String, Object> resultMap = skuFeign.search(searchMap).getData();
        //2.将数据设置到model中     (模板文件中 根据th:标签数据展示)
        //搜索的结果设置
        model.addAttribute("result", resultMap);


        //创建一个分页的对象  可以获取当前页 和总个记录数和显示的页码(以当前页为中心的5个页码)
        Page<SkuInfo> infoPage = new Page<SkuInfo>(
                Long.parseLong(resultMap.get("total").toString()),
                Integer.parseInt(resultMap.get("pageNum").toString()) + 1,
                Integer.parseInt(resultMap.get("pageSize").toString())
        );

        model.addAttribute("page",infoPage);

        //3.设置搜索的条件 回显
        model.addAttribute("searchMap",searchMap);

        //4.记住之前的URL
        //拼接url
        String[] urls = url(searchMap);
        model.addAttribute("url",urls[0]);
        model.addAttribute("sortUrl",urls[1]);

        //3.返回
        return "search";
    }

    private String[] url(Map<String, String> searchMap) {
        StringBuilder url = new StringBuilder("/search/list");
        StringBuilder sortUrl = new StringBuilder("/search/list");
        if(searchMap!=null && searchMap.size()>0){
            url.append("?");
            sortUrl.append("?");
            for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
                String key = stringStringEntry.getKey();// keywords / brand  / category
                String value = stringStringEntry.getValue();//华为  / 华为  / 笔记本
                if(key.equals("pageNum")){
                    continue;
                }
                url.append(key).append("=").append(value).append("&");

                if(! key.equalsIgnoreCase("sortField") && ! key.equalsIgnoreCase("sortRule"))
                    sortUrl.append(key).append("=").append(value).append("&");
            }

            //去掉多余的&
            if(url.lastIndexOf("&")!=-1){
               url = new StringBuilder(url.substring(0, url.lastIndexOf("&")));
            }

            if(sortUrl.lastIndexOf("&")!=-1){
                sortUrl = new StringBuilder(sortUrl.substring(0, sortUrl.lastIndexOf("&")));
            }

        }
        return new String[]{url.toString(), sortUrl.toString()};
    }
}
