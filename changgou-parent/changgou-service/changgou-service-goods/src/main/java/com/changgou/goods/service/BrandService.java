package com.changgou.goods.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author Xu Rui
 * @date 2021/2/19 22:05
 */
public interface BrandService {

    /**
     * 分页条件查询
     * @param brand brand
     * @param page  当前页
     * @param size  每页显示条数
     * @return      PageInfo<Brand>
     */
    PageInfo<Brand> findPage(Brand brand, Integer page, Integer size);

    /**
     * 分页查询
     * @param page  当前页
     * @param size  每页显示条数
     * @return      PageInfo<Brand>
     */
    PageInfo<Brand> findPage(Integer page, Integer size);

    /**
     * 按条件搜索品牌
     * @param brand brand
     * @return      List<Brand>
     */
    List<Brand> findList(Brand brand);

    /**
     * 根据id删除品牌
     * @param id    id
     */
    void delete(Integer id);

    /**
     * 根据id修改品牌信息
     * @param brand brand
     */
    void update(Brand brand);

    /**
     * 根据id查询
     * @param id    id
     * @return      返回brand
     */
    Brand findById(Integer id);

    /**
     * 查询所有品牌
     * @return  返回所有品牌的List集合
     */
    List<Brand> findAll();

    /**
     * 增加品牌
     * @param brand brand对象
     */
    void add(Brand brand);
}
