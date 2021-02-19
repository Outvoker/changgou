package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Xu Rui
 * @date 2021/2/19 21:28
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.changgou.dao"})  //开启通用Mapper的包扫描
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class);
    }
}
