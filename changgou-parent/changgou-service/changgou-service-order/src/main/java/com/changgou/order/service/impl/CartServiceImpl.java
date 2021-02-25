package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xu Rui
 * @date 2021/2/25 13:37
 */
@Service
public class CartServiceImpl implements CartService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SkuFeign skuFeign;

    @Resource
    private SpuFeign spuFeign;


    /***
     * 加入购物车
     * @param num:购买商品数量
     * @param id：购买ID
     * @param username：购买用户
     * @return
     */
    @Override
    public void add(Integer num, Long id, String username) {
        //当添加购物车数量<=0时，需要移除该商品信息
        String cartKey = "Cart_"+username;
        if(num <= 0){
            redisTemplate.boundHashOps(cartKey).delete(id);

            Long size = redisTemplate.boundHashOps(cartKey).size();
            if(size == null || size <= 0)
                redisTemplate.delete(cartKey);

            return;
        }

        //查询SKU
        Result<Sku> resultSku = skuFeign.findById(id);
        if(resultSku!=null && resultSku.isFlag()){
            //获取SKU
            Sku sku = resultSku.getData();
            //获取SPU
            Result<Spu> resultSpu = spuFeign.findById(sku.getSpuId());

            //将SKU转换成OrderItem
            OrderItem orderItem = sku2OrderItem(sku,resultSpu.getData(), num);

            /******
             * 购物车数据存入到Redis
             * namespace = Cart_[username]
             * key=id(sku)
             * value=OrderItem
             */
            redisTemplate.boundHashOps("Cart_"+username).put(id,orderItem);
        }
    }

    /***
     * 查询用户购物车数据
     * @param username
     * @return
     */
    @Override
    public List<OrderItem> list(String username) {
        //查询所有购物车数据
        List<OrderItem> orderItems = redisTemplate.boundHashOps("Cart_"+username).values();
        return orderItems;
    }

    /***
     * SKU转成OrderItem
     * @param sku
     * @param num
     * @return
     */
    private OrderItem sku2OrderItem(Sku sku,Spu spu,Integer num){
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num*orderItem.getPrice());       //单价*数量
        orderItem.setPayMoney(num*orderItem.getPrice());    //实付金额
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*num);           //重量=单个重量*数量

        //分类ID设置
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}