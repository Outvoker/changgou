package com.changgou.goods.service.impl;

import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xu Rui
 * @date 2021/2/19 22:05
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    /***
     * 根据分类ID查询品牌集合
     * @param categoryid:分类ID
     * @return
     */
    @Override
    public List<Brand> findByCategory(Integer categoryid) {
        //1.查询当前分类所对应的所有品牌信息
        //2.根据品牌ID查询对应的品牌集合

        //自己创建DAO实现查询
        return brandMapper.findByCategory(categoryid);
    }

    @Override
    public PageInfo<Brand> findPage(Brand brand, Integer page, Integer size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索数据
        Example example = createExample(brand);
        List<Brand> brands = brandMapper.selectByExample(example);
        //封装PageInfo
        return new PageInfo<>(brands);
    }

    /**
     * 分页查询
     * @param page  当前页
     * @param size  每页显示条数
     * @return      PageInfo<Brand>
     */
    @Override
    public PageInfo<Brand> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Brand> brands = brandMapper.selectAll();
        return new PageInfo<>(brands);
    }

    /**
     * 按条件搜索品牌
     * @param brand brand
     * @return      List<Brand>
     */
    @Override
    public List<Brand> findList(Brand brand) {
        Example example = createExample(brand);

        return brandMapper.selectByExample(example);
    }

    public Example createExample(Brand brand){
        //自定义搜索条件
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();   //条件构造器
        if (brand != null){
            //brand.name != null 根据名字模糊搜索
            if(!StringUtils.isEmpty(brand.getName())){
                criteria.andLike("name", "%" + brand.getName() + "%");
            }
            //brand.letter != null 根据首字母搜索
            if(!StringUtils.isEmpty(brand.getLetter())){
                criteria.andEqualTo("letter", brand.getLetter());
            }
        }
        return example;
    }

    /**
     * 根据id删除品牌
     * @param id    id
     */
    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据id修改品牌信息
     * @param brand brand
     */
    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    /**
     * 根据id查询
     * @param id    id
     * @return      返回brand
     */
    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有品牌
     * @return  返回所有品牌的List集合
     */
    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    /**
     * 增加品牌
     * @param brand brand对象
     */
    @Override
    public void add(Brand brand) {
        //Selective 表示会忽略空值
        brandMapper.insertSelective(brand);
    }
}
