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
 * @Description:ReturnOrder构建
 *
 *****/
@ApiModel(description = "ReturnOrder",value = "ReturnOrder")
@Table(name="tb_return_order")
@Data
public class ReturnOrder implements Serializable{

	@ApiModelProperty(value = "服务单号",required = false)
	@Id
    @Column(name = "id")
	private Long id;//服务单号

	@ApiModelProperty(value = "订单号",required = false)
    @Column(name = "order_id")
	private Long orderId;//订单号

	@ApiModelProperty(value = "申请时间",required = false)
    @Column(name = "apply_time")
	private Date applyTime;//申请时间

	@ApiModelProperty(value = "用户ID",required = false)
    @Column(name = "user_id")
	private Long userId;//用户ID

	@ApiModelProperty(value = "用户账号",required = false)
    @Column(name = "user_account")
	private String userAccount;//用户账号

	@ApiModelProperty(value = "联系人",required = false)
    @Column(name = "linkman")
	private String linkman;//联系人

	@ApiModelProperty(value = "联系人手机",required = false)
    @Column(name = "linkman_mobile")
	private String linkmanMobile;//联系人手机

	@ApiModelProperty(value = "类型",required = false)
    @Column(name = "type")
	private String type;//类型

	@ApiModelProperty(value = "退款金额",required = false)
    @Column(name = "return_money")
	private Integer returnMoney;//退款金额

	@ApiModelProperty(value = "是否退运费",required = false)
    @Column(name = "is_return_freight")
	private String isReturnFreight;//是否退运费

	@ApiModelProperty(value = "申请状态",required = false)
    @Column(name = "status")
	private String status;//申请状态

	@ApiModelProperty(value = "处理时间",required = false)
    @Column(name = "dispose_time")
	private Date disposeTime;//处理时间

	@ApiModelProperty(value = "退货退款原因",required = false)
    @Column(name = "return_cause")
	private Integer returnCause;//退货退款原因

	@ApiModelProperty(value = "凭证图片",required = false)
    @Column(name = "evidence")
	private String evidence;//凭证图片

	@ApiModelProperty(value = "问题描述",required = false)
    @Column(name = "description")
	private String description;//问题描述

	@ApiModelProperty(value = "处理备注",required = false)
    @Column(name = "remark")
	private String remark;//处理备注

	@ApiModelProperty(value = "管理员id",required = false)
    @Column(name = "admin_id")
	private Integer adminId;//管理员id




}
