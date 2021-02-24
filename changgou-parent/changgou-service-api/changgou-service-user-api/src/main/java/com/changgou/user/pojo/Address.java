package com.changgou.user.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:Address构建
 *
 *****/
@ApiModel(description = "Address",value = "Address")
@Table(name="tb_address")
@Data
public class Address implements Serializable{

	@ApiModelProperty(value = "",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//

	@ApiModelProperty(value = "用户名",required = false)
    @Column(name = "username")
	private String username;//用户名

	@ApiModelProperty(value = "省",required = false)
    @Column(name = "provinceid")
	private String provinceid;//省

	@ApiModelProperty(value = "市",required = false)
    @Column(name = "cityid")
	private String cityid;//市

	@ApiModelProperty(value = "县/区",required = false)
    @Column(name = "areaid")
	private String areaid;//县/区

	@ApiModelProperty(value = "电话",required = false)
    @Column(name = "phone")
	private String phone;//电话

	@ApiModelProperty(value = "详细地址",required = false)
    @Column(name = "address")
	private String address;//详细地址

	@ApiModelProperty(value = "联系人",required = false)
    @Column(name = "contact")
	private String contact;//联系人

	@ApiModelProperty(value = "是否是默认 1默认 0否",required = false)
    @Column(name = "is_default")
	private String isDefault;//是否是默认 1默认 0否

	@ApiModelProperty(value = "别名",required = false)
    @Column(name = "alias")
	private String alias;//别名




}
