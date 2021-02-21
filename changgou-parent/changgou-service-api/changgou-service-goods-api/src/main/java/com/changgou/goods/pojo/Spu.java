package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:Spu构建
 *
 *****/
@ApiModel(description = "Spu",value = "Spu")
@Table(name="tb_spu")
@Data
public class Spu implements Serializable{

	@ApiModelProperty(value = "主键",required = false)
	@Id
    @Column(name = "id")
	private Long id;//主键

	@ApiModelProperty(value = "货号",required = false)
    @Column(name = "sn")
	private String sn;//货号

	@ApiModelProperty(value = "SPU名",required = false)
    @Column(name = "name")
	private String name;//SPU名

	@ApiModelProperty(value = "副标题",required = false)
    @Column(name = "caption")
	private String caption;//副标题

	@ApiModelProperty(value = "品牌ID",required = false)
    @Column(name = "brand_id")
	private Integer brandId;//品牌ID

	@ApiModelProperty(value = "一级分类",required = false)
    @Column(name = "category1_id")
	private Integer category1Id;//一级分类

	@ApiModelProperty(value = "二级分类",required = false)
    @Column(name = "category2_id")
	private Integer category2Id;//二级分类

	@ApiModelProperty(value = "三级分类",required = false)
    @Column(name = "category3_id")
	private Integer category3Id;//三级分类

	@ApiModelProperty(value = "模板ID",required = false)
    @Column(name = "template_id")
	private Integer templateId;//模板ID

	@ApiModelProperty(value = "运费模板id",required = false)
    @Column(name = "freight_id")
	private Integer freightId;//运费模板id

	@ApiModelProperty(value = "图片",required = false)
    @Column(name = "image")
	private String image;//图片

	@ApiModelProperty(value = "图片列表",required = false)
    @Column(name = "images")
	private String images;//图片列表

	@ApiModelProperty(value = "售后服务",required = false)
    @Column(name = "sale_service")
	private String saleService;//售后服务

	@ApiModelProperty(value = "介绍",required = false)
    @Column(name = "introduction")
	private String introduction;//介绍

	@ApiModelProperty(value = "规格列表",required = false)
    @Column(name = "spec_items")
	private String specItems;//规格列表

	@ApiModelProperty(value = "参数列表",required = false)
    @Column(name = "para_items")
	private String paraItems;//参数列表

	@ApiModelProperty(value = "销量",required = false)
    @Column(name = "sale_num")
	private Integer saleNum;//销量

	@ApiModelProperty(value = "评论数",required = false)
    @Column(name = "comment_num")
	private Integer commentNum;//评论数

	@ApiModelProperty(value = "是否上架,0已下架，1已上架",required = false)
    @Column(name = "is_marketable")
	private String isMarketable;//是否上架,0已下架，1已上架

	@ApiModelProperty(value = "是否启用规格",required = false)
    @Column(name = "is_enable_spec")
	private String isEnableSpec;//是否启用规格

	@ApiModelProperty(value = "是否删除,0:未删除，1：已删除",required = false)
    @Column(name = "is_delete")
	private String isDelete;//是否删除,0:未删除，1：已删除

	@ApiModelProperty(value = "审核状态，0：未审核，1：已审核，2：审核不通过",required = false)
    @Column(name = "status")
	private String status;//审核状态，0：未审核，1：已审核，2：审核不通过




}
