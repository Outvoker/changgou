package com.changgou.user.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:OauthClientDetails构建
 *
 *****/
@ApiModel(description = "OauthClientDetails",value = "OauthClientDetails")
@Table(name="oauth_client_details")
@Data
public class OauthClientDetails implements Serializable{

	@ApiModelProperty(value = "客户端ID，主要用于标识对应的应用",required = false)
	@Id
    @Column(name = "client_id")
	private String clientId;//客户端ID，主要用于标识对应的应用

	@ApiModelProperty(value = "",required = false)
    @Column(name = "resource_ids")
	private String resourceIds;//

	@ApiModelProperty(value = "客户端秘钥，BCryptPasswordEncoder加密算法加密",required = false)
    @Column(name = "client_secret")
	private String clientSecret;//客户端秘钥，BCryptPasswordEncoder加密算法加密

	@ApiModelProperty(value = "对应的范围",required = false)
    @Column(name = "scope")
	private String scope;//对应的范围

	@ApiModelProperty(value = "认证模式",required = false)
    @Column(name = "authorized_grant_types")
	private String authorizedGrantTypes;//认证模式

	@ApiModelProperty(value = "认证后重定向地址",required = false)
    @Column(name = "web_server_redirect_uri")
	private String webServerRedirectUri;//认证后重定向地址

	@ApiModelProperty(value = "",required = false)
    @Column(name = "authorities")
	private String authorities;//

	@ApiModelProperty(value = "令牌有效期",required = false)
    @Column(name = "access_token_validity")
	private Integer accessTokenValidity;//令牌有效期

	@ApiModelProperty(value = "令牌刷新周期",required = false)
    @Column(name = "refresh_token_validity")
	private Integer refreshTokenValidity;//令牌刷新周期

	@ApiModelProperty(value = "",required = false)
    @Column(name = "additional_information")
	private String additionalInformation;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "autoapprove")
	private String autoapprove;//




}
