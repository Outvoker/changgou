package com.changgou.order.pojo;

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
 * @Description:OrderLog构建
 *
 *****/
@ApiModel(description = "OrderLog",value = "OrderLog")
@Table(name="tb_order_log")
@Data
public class OrderLog implements Serializable{

	@ApiModelProperty(value = "ID",required = false)
	@Id
    @Column(name = "id")
	private String id;//ID

	@ApiModelProperty(value = "操作员",required = false)
    @Column(name = "operater")
	private String operater;//操作员

	@ApiModelProperty(value = "操作时间",required = false)
    @Column(name = "operate_time")
	private Date operateTime;//操作时间

	@ApiModelProperty(value = "订单ID",required = false)
    @Column(name = "order_id")
	private String orderId;//订单ID

	@ApiModelProperty(value = "订单状态,0未完成，1已完成，2，已退货",required = false)
    @Column(name = "order_status")
	private String orderStatus;//订单状态,0未完成，1已完成，2，已退货

	@ApiModelProperty(value = "付款状态  0:未支付，1：已支付，2：支付失败",required = false)
    @Column(name = "pay_status")
	private String payStatus;//付款状态  0:未支付，1：已支付，2：支付失败

	@ApiModelProperty(value = "发货状态",required = false)
    @Column(name = "consign_status")
	private String consignStatus;//发货状态

	@ApiModelProperty(value = "备注",required = false)
    @Column(name = "remarks")
	private String remarks;//备注

	@ApiModelProperty(value = "支付金额",required = false)
    @Column(name = "money")
	private Integer money;//支付金额

	@ApiModelProperty(value = "",required = false)
    @Column(name = "username")
	private String username;//




}
