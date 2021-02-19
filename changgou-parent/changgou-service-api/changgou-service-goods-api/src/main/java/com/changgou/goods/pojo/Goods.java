package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品组合实体类
 */
@ApiModel(description = "商品信息")
@Data
public class Goods implements Serializable {
    //SPU
    private Spu spu;
    //SKU集合
    private List<Sku> skuList;

}