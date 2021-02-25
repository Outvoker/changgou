package com.changgou.order.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/****
 * @author Xu Rui
 * @Description:Preferential构建
 *
 *****/
@ApiModel(description = "Preferential",value = "Preferential")
@Table(name="tb_preferential")
@Data
public class Preferential implements Serializable{

	@ApiModelProperty(value = "ID",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//ID

	@ApiModelProperty(value = "消费金额",required = false)
    @Column(name = "buy_money")
	private Integer buyMoney;//消费金额

	@ApiModelProperty(value = "优惠金额",required = false)
    @Column(name = "pre_money")
	private Integer preMoney;//优惠金额

	@ApiModelProperty(value = "品类ID",required = false)
    @Column(name = "category_id")
	private Long categoryId;//品类ID

	@ApiModelProperty(value = "活动开始日期",required = false)
    @Column(name = "start_time")
	private Date startTime;//活动开始日期

	@ApiModelProperty(value = "活动截至日期",required = false)
    @Column(name = "end_time")
	private Date endTime;//活动截至日期

	@ApiModelProperty(value = "状态",required = false)
    @Column(name = "state")
	private String state;//状态

	@ApiModelProperty(value = "类型1不翻倍 2翻倍",required = false)
    @Column(name = "type")
	private String type;//类型1不翻倍 2翻倍




}
