package com.changgou.order.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:OrderItem构建
 *
 *****/
@ApiModel(description = "OrderItem",value = "OrderItem")
@Table(name="tb_order_item")
@Data
public class OrderItem implements Serializable{

	@ApiModelProperty(value = "ID",required = false)
	@Id
    @Column(name = "id")
	private String id;//ID

	@ApiModelProperty(value = "1级分类",required = false)
    @Column(name = "category_id1")
	private Integer categoryId1;//1级分类

	@ApiModelProperty(value = "2级分类",required = false)
    @Column(name = "category_id2")
	private Integer categoryId2;//2级分类

	@ApiModelProperty(value = "3级分类",required = false)
    @Column(name = "category_id3")
	private Integer categoryId3;//3级分类

	@ApiModelProperty(value = "SPU_ID",required = false)
    @Column(name = "spu_id")
	private Long spuId;//SPU_ID

	@ApiModelProperty(value = "SKU_ID",required = false)
    @Column(name = "sku_id")
	private Long skuId;//SKU_ID

	@ApiModelProperty(value = "订单ID",required = false)
    @Column(name = "order_id")
	private String orderId;//订单ID

	@ApiModelProperty(value = "商品名称",required = false)
    @Column(name = "name")
	private String name;//商品名称

	@ApiModelProperty(value = "单价",required = false)
    @Column(name = "price")
	private Integer price;//单价

	@ApiModelProperty(value = "数量",required = false)
    @Column(name = "num")
	private Integer num;//数量

	@ApiModelProperty(value = "总金额",required = false)
    @Column(name = "money")
	private Integer money;//总金额

	@ApiModelProperty(value = "实付金额",required = false)
    @Column(name = "pay_money")
	private Integer payMoney;//实付金额

	@ApiModelProperty(value = "图片地址",required = false)
    @Column(name = "image")
	private String image;//图片地址

	@ApiModelProperty(value = "重量",required = false)
    @Column(name = "weight")
	private Integer weight;//重量

	@ApiModelProperty(value = "运费",required = false)
    @Column(name = "post_fee")
	private Integer postFee;//运费

	@ApiModelProperty(value = "是否退货,0:未退货，1：已退货",required = false)
    @Column(name = "is_return")
	private String isReturn;//是否退货,0:未退货，1：已退货




}
