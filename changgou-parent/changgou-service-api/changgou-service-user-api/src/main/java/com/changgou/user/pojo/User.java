package com.changgou.user.pojo;

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
 * @Description:User构建
 *
 *****/
@ApiModel(description = "User",value = "User")
@Table(name="tb_user")
@Data
public class User implements Serializable{

	@ApiModelProperty(value = "用户名",required = false)
	@Id
    @Column(name = "username")
	private String username;//用户名

	@ApiModelProperty(value = "密码，加密存储",required = false)
    @Column(name = "password")
	private String password;//密码，加密存储

	@ApiModelProperty(value = "注册手机号",required = false)
    @Column(name = "phone")
	private String phone;//注册手机号

	@ApiModelProperty(value = "注册邮箱",required = false)
    @Column(name = "email")
	private String email;//注册邮箱

	@ApiModelProperty(value = "创建时间",required = false)
    @Column(name = "created")
	private Date created;//创建时间

	@ApiModelProperty(value = "修改时间",required = false)
    @Column(name = "updated")
	private Date updated;//修改时间

	@ApiModelProperty(value = "会员来源：1:PC，2：H5，3：Android，4：IOS",required = false)
    @Column(name = "source_type")
	private String sourceType;//会员来源：1:PC，2：H5，3：Android，4：IOS

	@ApiModelProperty(value = "昵称",required = false)
    @Column(name = "nick_name")
	private String nickName;//昵称

	@ApiModelProperty(value = "真实姓名",required = false)
    @Column(name = "name")
	private String name;//真实姓名

	@ApiModelProperty(value = "使用状态（1正常 0非正常）",required = false)
    @Column(name = "status")
	private String status;//使用状态（1正常 0非正常）

	@ApiModelProperty(value = "头像地址",required = false)
    @Column(name = "head_pic")
	private String headPic;//头像地址

	@ApiModelProperty(value = "QQ号码",required = false)
    @Column(name = "qq")
	private String qq;//QQ号码

	@ApiModelProperty(value = "手机是否验证 （0否  1是）",required = false)
    @Column(name = "is_mobile_check")
	private String isMobileCheck;//手机是否验证 （0否  1是）

	@ApiModelProperty(value = "邮箱是否检测（0否  1是）",required = false)
    @Column(name = "is_email_check")
	private String isEmailCheck;//邮箱是否检测（0否  1是）

	@ApiModelProperty(value = "性别，1男，0女",required = false)
    @Column(name = "sex")
	private String sex;//性别，1男，0女

	@ApiModelProperty(value = "会员等级",required = false)
    @Column(name = "user_level")
	private Integer userLevel;//会员等级

	@ApiModelProperty(value = "积分",required = false)
    @Column(name = "points")
	private Integer points;//积分

	@ApiModelProperty(value = "经验值",required = false)
    @Column(name = "experience_value")
	private Integer experienceValue;//经验值

	@ApiModelProperty(value = "出生年月日",required = false)
    @Column(name = "birthday")
	private Date birthday;//出生年月日

	@ApiModelProperty(value = "最后登录时间",required = false)
    @Column(name = "last_login_time")
	private Date lastLoginTime;//最后登录时间




}
