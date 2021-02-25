package com.changgou.order.controller;

import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import entity.TokenDecode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Xu Rui
 * @date 2021/2/25 13:36
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/cart")
public class CartController {

    @Resource
    private CartService cartService;

    /***
     * 查询用户购物车列表
     * @return
     */
    @GetMapping(value = "/list")
    public Result list(){
        //用户名
//        String username="szitheima";
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        String username = userInfo.get("username");
        List<OrderItem> orderItems = cartService.list(username);
        return new Result(true,StatusCode.OK,"购物车列表查询成功！",orderItems);
    }

    /***
     * 加入购物车
     * @param num:购买的数量
     * @param id：购买的商品(SKU)ID
     * @return
     */
    @RequestMapping(value = "/add")
    public Result add(Integer num, Long id){
        //用户名
        String username="szitheima";
        //将商品加入购物车
        cartService.add(num,id,username);
        return new Result(true, StatusCode.OK,"加入购物车成功！");
    }
}