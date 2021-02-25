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
 * @Description:Order构建
 *
 *****/
@ApiModel(description = "Order",value = "Order")
@Table(name="tb_order")
@Data
public class Order implements Serializable{

	@ApiModelProperty(value = "订单id",required = false)
	@Id
    @Column(name = "id")
	private String id;//订单id

	@ApiModelProperty(value = "数量合计",required = false)
    @Column(name = "total_num")
	private Integer totalNum;//数量合计

	@ApiModelProperty(value = "金额合计",required = false)
    @Column(name = "total_money")
	private Integer totalMoney;//金额合计

	@ApiModelProperty(value = "优惠金额",required = false)
    @Column(name = "pre_money")
	private Integer preMoney;//优惠金额

	@ApiModelProperty(value = "邮费",required = false)
    @Column(name = "post_fee")
	private Integer postFee;//邮费

	@ApiModelProperty(value = "实付金额",required = false)
    @Column(name = "pay_money")
	private Integer payMoney;//实付金额

	@ApiModelProperty(value = "支付类型，1、在线支付、0 货到付款",required = false)
    @Column(name = "pay_type")
	private String payType;//支付类型，1、在线支付、0 货到付款

	@ApiModelProperty(value = "订单创建时间",required = false)
    @Column(name = "create_time")
	private Date createTime;//订单创建时间

	@ApiModelProperty(value = "订单更新时间",required = false)
    @Column(name = "update_time")
	private Date updateTime;//订单更新时间

	@ApiModelProperty(value = "付款时间",required = false)
    @Column(name = "pay_time")
	private Date payTime;//付款时间

	@ApiModelProperty(value = "发货时间",required = false)
    @Column(name = "consign_time")
	private Date consignTime;//发货时间

	@ApiModelProperty(value = "交易完成时间",required = false)
    @Column(name = "end_time")
	private Date endTime;//交易完成时间

	@ApiModelProperty(value = "交易关闭时间",required = false)
    @Column(name = "close_time")
	private Date closeTime;//交易关闭时间

	@ApiModelProperty(value = "物流名称",required = false)
    @Column(name = "shipping_name")
	private String shippingName;//物流名称

	@ApiModelProperty(value = "物流单号",required = false)
    @Column(name = "shipping_code")
	private String shippingCode;//物流单号

	@ApiModelProperty(value = "用户名称",required = false)
    @Column(name = "username")
	private String username;//用户名称

	@ApiModelProperty(value = "买家留言",required = false)
    @Column(name = "buyer_message")
	private String buyerMessage;//买家留言

	@ApiModelProperty(value = "是否评价",required = false)
    @Column(name = "buyer_rate")
	private String buyerRate;//是否评价

	@ApiModelProperty(value = "收货人",required = false)
    @Column(name = "receiver_contact")
	private String receiverContact;//收货人

	@ApiModelProperty(value = "收货人手机",required = false)
    @Column(name = "receiver_mobile")
	private String receiverMobile;//收货人手机

	@ApiModelProperty(value = "收货人地址",required = false)
    @Column(name = "receiver_address")
	private String receiverAddress;//收货人地址

	@ApiModelProperty(value = "订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面",required = false)
    @Column(name = "source_type")
	private String sourceType;//订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面

	@ApiModelProperty(value = "交易流水号",required = false)
    @Column(name = "transaction_id")
	private String transactionId;//交易流水号

	@ApiModelProperty(value = "订单状态,0:未完成,1:已完成，2：已退货",required = false)
    @Column(name = "order_status")
	private String orderStatus;//订单状态,0:未完成,1:已完成，2：已退货

	@ApiModelProperty(value = "支付状态,0:未支付，1：已支付，2：支付失败",required = false)
    @Column(name = "pay_status")
	private String payStatus;//支付状态,0:未支付，1：已支付，2：支付失败

	@ApiModelProperty(value = "发货状态,0:未发货，1：已发货，2：已收货",required = false)
    @Column(name = "consign_status")
	private String consignStatus;//发货状态,0:未发货，1：已发货，2：已收货

	@ApiModelProperty(value = "是否删除",required = false)
    @Column(name = "is_delete")
	private String isDelete;//是否删除




}
