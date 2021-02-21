package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/****
 * @author Xu Rui
 * @Description:Sku构建
 *
 *****/
@ApiModel(description = "Sku",value = "Sku")
@Table(name="tb_sku")
@Data
public class Sku implements Serializable{

	@ApiModelProperty(value = "商品id",required = false)
	@Id
    @Column(name = "id")
	private Long id;//商品id

	@ApiModelProperty(value = "商品条码",required = false)
    @Column(name = "sn")
	private String sn;//商品条码

	@ApiModelProperty(value = "SKU名称",required = false)
    @Column(name = "name")
	private String name;//SKU名称

	@ApiModelProperty(value = "价格（分）",required = false)
    @Column(name = "price")
	private Integer price;//价格（分）

	@ApiModelProperty(value = "库存数量",required = false)
    @Column(name = "num")
	private Integer num;//库存数量

	@ApiModelProperty(value = "库存预警数量",required = false)
    @Column(name = "alert_num")
	private Integer alertNum;//库存预警数量

	@ApiModelProperty(value = "商品图片",required = false)
    @Column(name = "image")
	private String image;//商品图片

	@ApiModelProperty(value = "商品图片列表",required = false)
    @Column(name = "images")
	private String images;//商品图片列表

	@ApiModelProperty(value = "重量（克）",required = false)
    @Column(name = "weight")
	private Integer weight;//重量（克）

	@ApiModelProperty(value = "创建时间",required = false)
    @Column(name = "create_time")
	private Date createTime;//创建时间

	@ApiModelProperty(value = "更新时间",required = false)
    @Column(name = "update_time")
	private Date updateTime;//更新时间

	@ApiModelProperty(value = "SPUID",required = false)
    @Column(name = "spu_id")
	private Long spuId;//SPUID

	@ApiModelProperty(value = "类目ID",required = false)
    @Column(name = "category_id")
	private Integer categoryId;//类目ID

	@ApiModelProperty(value = "类目名称",required = false)
    @Column(name = "category_name")
	private String categoryName;//类目名称

	@ApiModelProperty(value = "品牌名称",required = false)
    @Column(name = "brand_name")
	private String brandName;//品牌名称

	@ApiModelProperty(value = "规格",required = false)
    @Column(name = "spec")
	private String spec;//规格

	@ApiModelProperty(value = "销量",required = false)
    @Column(name = "sale_num")
	private Integer saleNum;//销量

	@ApiModelProperty(value = "评论数",required = false)
    @Column(name = "comment_num")
	private Integer commentNum;//评论数

	@ApiModelProperty(value = "商品状态 1-正常，2-下架，3-删除",required = false)
    @Column(name = "status")
	private String status;//商品状态 1-正常，2-下架，3-删除




}
