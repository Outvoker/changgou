package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import entity.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Xu Rui
 * @date 2021/2/25 11:35
 */
@FeignClient(value = "user")
@RequestMapping(value = "/user")
public interface UserFeign {

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @GetMapping({"/load/{id}"})
    Result<User> findById(@PathVariable String id);
}
