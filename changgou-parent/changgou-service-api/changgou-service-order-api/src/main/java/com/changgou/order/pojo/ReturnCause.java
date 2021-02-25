package com.changgou.order.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:ReturnCause构建
 *
 *****/
@ApiModel(description = "ReturnCause",value = "ReturnCause")
@Table(name="tb_return_cause")
@Data
public class ReturnCause implements Serializable{

	@ApiModelProperty(value = "ID",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//ID

	@ApiModelProperty(value = "原因",required = false)
    @Column(name = "cause")
	private String cause;//原因

	@ApiModelProperty(value = "排序",required = false)
    @Column(name = "seq")
	private Integer seq;//排序

	@ApiModelProperty(value = "是否启用",required = false)
    @Column(name = "status")
	private String status;//是否启用




}
