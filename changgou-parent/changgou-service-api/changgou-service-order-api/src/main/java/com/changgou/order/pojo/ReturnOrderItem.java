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
 * @Description:ReturnOrderItem构建
 *
 *****/
@ApiModel(description = "ReturnOrderItem",value = "ReturnOrderItem")
@Table(name="tb_return_order_item")
@Data
public class ReturnOrderItem implements Serializable{

	@ApiModelProperty(value = "ID",required = false)
	@Id
    @Column(name = "id")
	private Long id;//ID

	@ApiModelProperty(value = "分类ID",required = false)
    @Column(name = "category_id")
	private Long categoryId;//分类ID

	@ApiModelProperty(value = "SPU_ID",required = false)
    @Column(name = "spu_id")
	private Long spuId;//SPU_ID

	@ApiModelProperty(value = "SKU_ID",required = false)
    @Column(name = "sku_id")
	private Long skuId;//SKU_ID

	@ApiModelProperty(value = "订单ID",required = false)
    @Column(name = "order_id")
	private Long orderId;//订单ID

	@ApiModelProperty(value = "订单明细ID",required = false)
    @Column(name = "order_item_id")
	private Long orderItemId;//订单明细ID

	@ApiModelProperty(value = "退货订单ID",required = false)
    @Column(name = "return_order_id")
	private Long returnOrderId;//退货订单ID

	@ApiModelProperty(value = "标题",required = false)
    @Column(name = "title")
	private String title;//标题

	@ApiModelProperty(value = "单价",required = false)
    @Column(name = "price")
	private Integer price;//单价

	@ApiModelProperty(value = "数量",required = false)
    @Column(name = "num")
	private Integer num;//数量

	@ApiModelProperty(value = "总金额",required = false)
    @Column(name = "money")
	private Integer money;//总金额

	@ApiModelProperty(value = "支付金额",required = false)
    @Column(name = "pay_money")
	private Integer payMoney;//支付金额

	@ApiModelProperty(value = "图片地址",required = false)
    @Column(name = "image")
	private String image;//图片地址

	@ApiModelProperty(value = "重量",required = false)
    @Column(name = "weight")
	private Integer weight;//重量




}
