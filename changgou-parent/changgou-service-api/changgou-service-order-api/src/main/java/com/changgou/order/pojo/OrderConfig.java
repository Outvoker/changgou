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
 * @Description:OrderConfig构建
 *
 *****/
@ApiModel(description = "OrderConfig",value = "OrderConfig")
@Table(name="tb_order_config")
@Data
public class OrderConfig implements Serializable{

	@ApiModelProperty(value = "ID",required = false)
	@Id
    @Column(name = "id")
	private Integer id;//ID

	@ApiModelProperty(value = "正常订单超时时间（分）",required = false)
    @Column(name = "order_timeout")
	private Integer orderTimeout;//正常订单超时时间（分）

	@ApiModelProperty(value = "秒杀订单超时时间（分）",required = false)
    @Column(name = "seckill_timeout")
	private Integer seckillTimeout;//秒杀订单超时时间（分）

	@ApiModelProperty(value = "自动收货（天）",required = false)
    @Column(name = "take_timeout")
	private Integer takeTimeout;//自动收货（天）

	@ApiModelProperty(value = "售后期限",required = false)
    @Column(name = "service_timeout")
	private Integer serviceTimeout;//售后期限

	@ApiModelProperty(value = "自动五星好评",required = false)
    @Column(name = "comment_timeout")
	private Integer commentTimeout;//自动五星好评




}
