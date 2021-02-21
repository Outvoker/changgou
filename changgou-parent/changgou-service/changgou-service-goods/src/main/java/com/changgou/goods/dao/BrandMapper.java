package com.changgou.goods.dao;

import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Xu Rui
 * @date 2021/2/19 22:05
 */
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 查询分类对应的品牌集合
     * @param categoryId
     * @return
     */
    @Select("SELECT tb.* FROM tb_category_brand tcb,tb_brand tb WHERE tcb.category_id=#{categoryId} AND tb.id=tcb.brand_id")
    List<Brand> findByCategory(Integer categoryId);
}
