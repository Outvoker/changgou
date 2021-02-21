package com.changgou.goods.controller;

import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xu Rui
 * @date 2021/2/19 22:06
 */
@RestController
@Slf4j
@RequestMapping("/brand")
@CrossOrigin    //跨域 允许跨域
public class BrandController {

    @Resource
    private BrandService brandService;

    /**
     * 根据条件查询品牌
     * @param brand 模糊条件
     * @return      Result<List<Brand>>
     */
    @PostMapping("/search")
    public Result<List<Brand>> findList(@RequestBody Brand brand){
        log.info("findList({})", brand);
        List<Brand> list = brandService.findList(brand);
        return new Result<>(true, StatusCode.OK, "条件搜索查询成功", list);
    }

    /**
     * 分页条件搜索
     * @param brand 模糊条件
     * @param page  当前页
     * @param size  每页条数
     * @return      Result<PageInfo<Brand>>
     */
    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@RequestBody Brand brand,
                                            @PathVariable("page") Integer page,
                                            @PathVariable("size") Integer size){
        log.info("findPage({}, {})", page, size);
        PageInfo<Brand> pageInfo = brandService.findPage(brand, page, size);
        return new Result<>(true, StatusCode.OK, "分页条件查询成功", pageInfo);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") Integer id){
        log.info("delete({})", id);
        brandService.delete(id);
        return new Result<>(true, StatusCode.OK, "删除品牌成功！", id);
    }

    /**
     * 根据id修改品牌
     * @param id    id
     * @param brand brand
     * @return      result
     */
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable("id") Integer id,
                            @RequestBody Brand brand){
        log.info("update({},{})", id, brand);
        brand.setId(id);
        brandService.update(brand);
        return new Result<>(true, StatusCode.OK, "修改品牌成功！", brand);
    }

    /**
     * 增加品牌
     * @param brand brand
     * @return      result
     */
    @PostMapping
    public Result<?> add(@RequestBody Brand brand){
        log.info("add({})", brand);
        brandService.add(brand);
        return new Result<>(true, StatusCode.OK, "增加品牌成功！", brand);
    }

    /**
     * 根据id查询品牌
     * @param id    id
     * @return      Result<Brand>
     */
    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable Integer id){
        log.info("findById({})", id);
        Brand brand = brandService.findById(id);
        return new Result<>(true, StatusCode.OK, "根据id查询品牌成功", brand);
    }

    /**
     * 查询所有品牌
     * @return  Result<List<Brand>>
     */
    @GetMapping
    public Result<List<Brand>> findAll(){
        log.info("findAll");
        List<Brand> list = brandService.findAll();
        return new Result<>(true, StatusCode.OK, "查询所有品牌信息成功！", list);
    }

    /**
     * 分页查询
     * @param page  当前页
     * @param size  每页条数
     * @return      Result<PageInfo<Brand>>
     */
    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@PathVariable("page") Integer page,
                                    @PathVariable("size") Integer size){
        log.info("findPage({}, {})", page, size);
        PageInfo<Brand> pageInfo = brandService.findPage(page, size);
        return new Result<>(true, StatusCode.OK, "分页查询成功", pageInfo);
    }
}
